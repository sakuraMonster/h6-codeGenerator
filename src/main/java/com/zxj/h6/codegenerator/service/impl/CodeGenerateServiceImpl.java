package com.zxj.h6.codegenerator.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.intellij.database.util.DasUtil;
import com.intellij.database.util.DbUtil;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.util.ReflectionUtil;
import com.sjhy.plugin.dict.GlobalDict;
import com.sjhy.plugin.dto.GenerateOptions;
import com.sjhy.plugin.dto.SettingsStorageDTO;
import com.sjhy.plugin.entity.Callback;
import com.sjhy.plugin.entity.ColumnInfo;
import com.sjhy.plugin.entity.SaveFile;
import com.sjhy.plugin.entity.TableInfo;
import com.sjhy.plugin.entity.Template;
import com.sjhy.plugin.entity.TypeMapper;
import com.sjhy.plugin.enums.MatchType;
import com.sjhy.plugin.service.SettingsStorageService;
import com.sjhy.plugin.service.TableInfoSettingsService;
import com.sjhy.plugin.tool.CacheDataUtils;
import com.sjhy.plugin.tool.CloneUtils;
import com.sjhy.plugin.tool.CollectionUtil;
import com.sjhy.plugin.tool.CurrGroupUtils;
import com.sjhy.plugin.tool.GlobalTool;
import com.sjhy.plugin.tool.ModuleUtils;
import com.sjhy.plugin.tool.NameUtils;
import com.sjhy.plugin.tool.TemplateUtils;
import com.sjhy.plugin.tool.TimeUtils;
import com.sjhy.plugin.tool.VelocityUtils;
import com.zxj.h6.codegenerator.service.CodeGenerateService;
import com.zxj.h6.codegenerator.tool.ExtraCodeGenerateUtils;
import com.zxj.h6.codegenerator.sqlparser.ColumnCategory;
import com.zxj.h6.codegenerator.sqlparser.SqlParser;
import com.zxj.h6.codegenerator.tool.ExtendsClassUtils;
import com.zxj.h6.codegenerator.tool.StrTool;
import com.zxj.h6.codegenerator.tool.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author zhangxuejun
 */
public class CodeGenerateServiceImpl implements CodeGenerateService {
    /**
     * 项目对象
     */
    private Project project;
    /**
     * 模型管理
     */
    private ModuleManager moduleManager;
    /**
     * 表信息服务
     */
    private TableInfoSettingsService tableInfoService;
    /**
     * 缓存数据工具
     */
    private CacheDataUtils cacheDataUtils;
    /**
     * 导入包时过滤的包前缀
     */
    private static final String FILTER_PACKAGE_NAME = "java.lang";

    public CodeGenerateServiceImpl(Project project) {
        this.project = project;
        this.moduleManager = ModuleManager.getInstance(project);
        this.tableInfoService = TableInfoSettingsService.getInstance();
        this.cacheDataUtils = CacheDataUtils.getInstance();
    }

    /**
     * 生成
     *
     * @param templates       模板
     * @param generateOptions 生成选项
     */
    @Override
    public void generate(Collection<Template> templates, GenerateOptions generateOptions, String sourceSql) {
        // 获取选中表信息
        TableInfo selectedTableInfo = tableInfoService.getTableInfo(cacheDataUtils.getSelectDbTable());;
        List<TableInfo> tableInfoList = cacheDataUtils.getDbTableList().stream().map(item -> tableInfoService.getTableInfo(item)).collect(Collectors.toList());
        // 校验选中表的保存路径是否正确
        if (StringUtils.isNullOrBlank(selectedTableInfo.getSavePath())) {
            if (selectedTableInfo.getObj() != null) {
                Messages.showInfoMessage(selectedTableInfo.getObj().getName() + "表配置信息不正确，请尝试重新配置", GlobalDict.TITLE_INFO);
            } else {
                Messages.showInfoMessage("配置信息不正确，请尝试重新配置", GlobalDict.TITLE_INFO);
            }
            return;
        }
        // 将未配置的表进行配置覆盖
        TableInfo finalSelectedTableInfo = selectedTableInfo;
        tableInfoList.forEach(tableInfo -> {
            if (StringUtils.isNullOrBlank(tableInfo.getSavePath())) {
                tableInfo.setSaveModelName(finalSelectedTableInfo.getSaveModelName());
                tableInfo.setSavePackageName(finalSelectedTableInfo.getSavePackageName());
                tableInfo.setSavePath(finalSelectedTableInfo.getSavePath());
                tableInfo.setPreName(finalSelectedTableInfo.getPreName());
                tableInfoService.saveTableInfo(tableInfo);
            }
        });
        // 如果使用统一配置，直接全部覆盖
        if (Boolean.TRUE.equals(generateOptions.getUnifiedConfig())) {
            tableInfoList.forEach(tableInfo -> {
                tableInfo.setSaveModelName(finalSelectedTableInfo.getSaveModelName());
                tableInfo.setSavePackageName(finalSelectedTableInfo.getSavePackageName());
                tableInfo.setSavePath(finalSelectedTableInfo.getSavePath());
                tableInfo.setPreName(finalSelectedTableInfo.getPreName());
            });
        }

        SqlParser sqlParser = new SqlParser(new StringBuffer());
        // 根据来源sql构建
        if(!StringUtils.isNullOrBlank(sourceSql)) {
            sqlParser = new SqlParser(new StringBuffer(sourceSql));
            SqlParser finalSqlParser = sqlParser;
            tableInfoList.forEach(tableInfo -> {
                String tableName = finalSqlParser.tableNameMap.get(tableInfo.getName().toLowerCase());
                if(tableName != null) {
                    tableInfo.setName(NameUtils.getInstance().firstUpperCase(tableName));
                }
                String tableComment = finalSqlParser.tableCommentMap.get(tableInfo.getName().toLowerCase());
                if(tableComment != null) {
                    tableInfo.setComment(tableComment);
                }

                List<ColumnCategory> rowSqlCategories = finalSqlParser.columnCategoriesMap.get(
                        tableInfo.getName().toLowerCase());
                if(rowSqlCategories != null) {
                    for(ColumnInfo columnInfo : tableInfo.getFullColumn()) {

                        Optional<ColumnCategory> category = rowSqlCategories.stream()
                                                                            .filter(columnCategory ->
                                                                                    columnCategory.getColumn().toLowerCase()
                                                                                                  .equals(columnInfo.getName().toLowerCase()))
                                                                            .findFirst();
                        if(category.isPresent()) {
                            ColumnCategory columnCategory = category.get();
                            columnInfo.setName(columnCategory.getColumn());
                            columnInfo.setComment(columnCategory.getComment());

                            // 字段类型
                            String type = columnCategory.getType().toLowerCase();
                            // 更新字段的扩展字段
                            updateColumnInfoExtactFields(columnInfo, type, columnCategory);
                            if(project.getName().startsWith("hdpos")) {
                                // 如果是hdpos, 重新设置javatype。
                                // oracle的dbtable无法读取字段长度
                                columnInfo.setType(getJavaType(type));
                            }
                        }
                    }
                }
            });



        }

        // 生成代码
        generate(templates, tableInfoList, generateOptions,  sqlParser, null);
    }

    private String getJavaType(String dbType) {
        for (TypeMapper typeMapper : CurrGroupUtils.getCurrTypeMapperGroup().getElementList()) {
            if (typeMapper.getMatchType() == MatchType.ORDINARY) {
                if (dbType.equalsIgnoreCase(typeMapper.getColumnType())) {
                    return typeMapper.getJavaType();
                }
            } else {
                // 不区分大小写的正则匹配模式
                if (Pattern.compile(typeMapper.getColumnType(), Pattern.CASE_INSENSITIVE).matcher(dbType).matches()) {
                    return typeMapper.getJavaType();
                }
            }
        }
        return "java.lang.Object";
    }

    private void updateColumnInfoExtactFields(ColumnInfo columnInfo, String type,
            ColumnCategory columnCategory) {
        if(type.startsWith("varchar")) {
            int start = type.indexOf("(");
            int end = type.indexOf(")");
            columnInfo.getExt().put("length", type.substring(start + 1, end));
        } else if((type.startsWith("number") ||
                type.startsWith("numeric")) && type.contains(",")) {
            int start = type.indexOf("(");
            int end = type.indexOf(")");
            String[] precisionAndScale = type.substring(start + 1, end).split(",");
            if(StringUtils.toInteger(precisionAndScale[1]) != 0) {
                columnInfo.getExt().put("precision", precisionAndScale[0]);
                columnInfo.getExt().put("scale", precisionAndScale[1]);
            }
        }
        // 是否为空
        if(!StringUtils.isNullOrBlank(columnCategory.getConstant())) {
            String constant = columnCategory.getConstant().toLowerCase();
            if (constant.contains("not null")) {
                columnInfo.getExt().put("nullable", "false");
            } else {
                columnInfo.getExt().put("nullable", "true");
            }
        } else {
            columnInfo.getExt().put("nullable", "true");
        }
    }

    private static void setCustumFieldType(TableInfo tableInfo, SqlParser sqlParser,
            List<TypeMapper> typeMapperList) {
        List<ColumnCategory> rowSqlCategories = sqlParser.columnCategoriesMap.get(
                tableInfo.getName().toLowerCase());
        if(rowSqlCategories != null) {
            for(ColumnInfo columnInfo : tableInfo.getFullColumn()) {

                Optional<ColumnCategory> category = rowSqlCategories.stream()
                                                                    .filter(columnCategory ->
                                                                            columnCategory.getColumn().toLowerCase()
                                                                                   .equals(columnInfo.getName().toLowerCase()))
                                                                    .findFirst();
                if(category.isPresent()) {
                    ColumnCategory columnCategory = category.get();
                    Optional<TypeMapper> mapper = typeMapperList.stream().filter(typeMapper ->
                                               typeMapper.getMatchType().equals(
                                                     MatchType.ORDINARY) && typeMapper.getColumnType().toLowerCase()
                                                    .equals(columnCategory.getColumn().toLowerCase()))
                                                   .findFirst();
                    if(mapper.isPresent()) {
                        TypeMapper typeMapper = mapper.get();
                        columnInfo.setType(typeMapper.getJavaType());
                    }
                }
            }
        }
    }

    /**
     * 生成代码，并自动保存到对应位置
     *
     * @param templates       模板
     * @param tableInfoList   表信息对象
     * @param generateOptions 生成配置
     * @param otherParam      其他参数
     */
    public void generate(Collection<Template> templates, Collection<TableInfo> tableInfoList,
            GenerateOptions generateOptions, SqlParser sqlParser, Map<String, Object> otherParam) {
        if (CollectionUtil.isEmpty(templates) || CollectionUtil.isEmpty(tableInfoList)) {
            return;
        }
        // 处理模板，注入全局变量（克隆一份，防止篡改）
        templates = CloneUtils.cloneByJson(templates, new TypeReference<ArrayList<Template>>() {
        });
        TemplateUtils.addGlobalConfig(templates);
        // 生成代码
        for (TableInfo tableInfo : tableInfoList) {
            // 表名去除前缀
            if (!StringUtils.isNullOrBlank(tableInfo.getPreName()) && tableInfo.getObj().getName().startsWith(tableInfo.getPreName())) {
                String newName = tableInfo.getObj().getName().substring(tableInfo.getPreName().length());
                tableInfo.setName(NameUtils.getInstance().getClassName(newName));
            }
            // 构建参数
            Map<String, Object> param = getDefaultParam();
            // 其他参数
            if (otherParam != null) {
                param.putAll(otherParam);
            }
            // 所有表信息对象
            param.put("tableInfoList", tableInfoList);
            // 表信息对象
            param.put("tableInfo", tableInfo);
            // 项目工程名称
            param.put("projectName", project.getName());

            // 设置额外代码生成服务
            param.put("generateService", new ExtraCodeGenerateUtils(this, tableInfo, generateOptions));
            for (Template template : templates) {
                // 判断模版是否是pEntity
                boolean isPEntity = false;
                if("pEntity.java.vm".toLowerCase().equals(template.getName().toLowerCase())) {
                    isPEntity = true;
                }

                // 非PEntity时需要转换
                if(!isPEntity) {
                    List<TypeMapper> typeMapperList = CurrGroupUtils.getCurrTypeMapperGroup().getElementList();
                    setCustumFieldType(tableInfo, sqlParser, typeMapperList);
                }

                ExtendsClassUtils extendsClassUtils = new ExtendsClassUtils(tableInfo, isPEntity);
                // 需要继承的对象
                String extendsClassName = extendsClassUtils.getExtendsClassName();
                List<String> ignoreFields = extendsClassUtils.getIgnoreFields();
                // 继承类
                param.put("extendsClassName", extendsClassName);
                // 继承类中需要忽略的字段
                param.put("ignoreFields", ignoreFields);
                // 设置模型路径与导包列表
                setModulePathAndImportList(param, tableInfo, isPEntity);

                Callback callback = new Callback();
                callback.setWriteFile(true);
                callback.setReformat(generateOptions.getReFormat());
                // 默认名称
                callback.setFileName(tableInfo.getName() + "Default.java");
                // 默认路径
                callback.setSavePath(tableInfo.getSavePath());
                // 设置回调对象
                param.put("callback", callback);
                // 开始生成
                String code = VelocityUtils.generate(template.getCode(), param);
                // 设置一个默认保存路径与默认文件名
                String path = callback.getSavePath();
                path = path.replace("\\", "/");
                // 针对相对路径进行处理
                if (path.startsWith(".")) {
                    path = project.getBasePath() + path.substring(1);
                }
                callback.setSavePath(path);
                new SaveFile(project, code, callback, generateOptions).write();
            }
        }
    }

    /**
     * 生成代码
     *
     * @param template  模板
     * @param tableInfo 表信息对象
     * @return 生成好的代码
     */
    @Override
    public String generate(Template template, TableInfo tableInfo) {
        // 获取默认参数
        Map<String, Object> param = getDefaultParam();
        // 表信息对象，进行克隆，防止篡改
        param.put("tableInfo", tableInfo);
        // 设置模型路径与导包列表
        setModulePathAndImportList(param, tableInfo, false);
        // 处理模板，注入全局变量
        TemplateUtils.addGlobalConfig(template);
        return VelocityUtils.generate(template.getCode(), param);
    }

    /**
     * 设置模型路径与导包列表
     *
     * @param param     参数
     * @param tableInfo 表信息对象
     */
    private void setModulePathAndImportList(Map<String, Object> param, TableInfo tableInfo, boolean isPEntity) {
        Module module = null;
        if (!StringUtils.isNullOrBlank(tableInfo.getSaveModelName())) {
            module = this.moduleManager.findModuleByName(tableInfo.getSaveModelName());
        }
        if (module != null) {
            // 设置modulePath
            param.put("modulePath", ModuleUtils.getModuleDir(module).getPath());
        }
        param.put("module", module.getName());
        // 设置要导入的包
        param.put("importList", getImportList(tableInfo, isPEntity));
    }

    /**
     * 获取默认参数
     *
     * @return 参数
     */
    private Map<String, Object> getDefaultParam() {
        // 系统设置
        SettingsStorageDTO settings = SettingsStorageService.getSettingsStorage();
        Map<String, Object> param = new HashMap<>(20);
        // 作者
        param.put("author", settings.getAuthor());
        //工具类
        param.put("tool", GlobalTool.getInstance());
        param.put("time", TimeUtils.getInstance());
        param.put("strTool", StrTool.getInstance());
        // 项目路径
        param.put("projectPath", project.getBasePath());
        // Database数据库工具
        param.put("dbUtil", ReflectionUtil.newInstance(DbUtil.class));
        param.put("dasUtil", ReflectionUtil.newInstance(DasUtil.class));
        return param;
    }

    /**
     * 获取导入列表
     *
     * @param tableInfo 表信息对象
     * @return 导入列表
     */
    private Set<String> getImportList(TableInfo tableInfo, boolean isPEntity) {
        // 创建一个自带排序的集合
        Set<String> result = new TreeSet<>();
        tableInfo.getFullColumn().forEach(columnInfo -> {
            if (!columnInfo.getType().startsWith(FILTER_PACKAGE_NAME)) {
                String type = NameUtils.getInstance().getClsFullNameRemoveGeneric(columnInfo.getType());
                result.add(type);
            }
        });

        String extendsClassName = new ExtendsClassUtils(tableInfo, isPEntity).getExtendsClassName();
        if(!StringUtils.isNullOrBlank(extendsClassName)) {
            result.add(NameUtils.getInstance().getClsFullNameRemoveGeneric(extendsClassName));
        }

        return result;
    }
}
