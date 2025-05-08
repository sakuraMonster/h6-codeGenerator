package com.zxj.h6.codegenerator.tool;

/**
 * 额外的代码生成工具
 *
 * @author zhangxuejun
 */
public class ExtraCodeGenerateUtils {
//    /**
//     * 代码生成服务
//     */
//    private CodeGenerateServiceImpl codeGenerateService;
//    /**
//     * 表信息对象
//     */
//    private TableInfo tableInfo;
//    /**
//     * 生成配置
//     */
//    private GenerateOption generateOption;
//
//    public ExtraCodeGenerateUtils(CodeGenerateServiceImpl codeGenerateService, TableInfo tableInfo, GenerateOption generateOption) {
//        this.codeGenerateService = codeGenerateService;
//        this.tableInfo = tableInfo;
//        this.generateOption = generateOption;
//    }
//
//    /**
//     * 生成代码
//     *
//     * @param templateName 模板名称
//     * @param param        附加参数
//     */
//    public void run(String templateName, Map<String, Object> param) {
//        // 获取到模板
//        Template currTemplate = null;
//        for (Template template : CurrGroupUtils.getCurrTemplateGroup().getElementList()) {
//            if (Objects.equals(template.getName(), templateName)) {
//                currTemplate = template;
//            }
//        }
//        if (currTemplate == null) {
//            return;
//        }
//        // 生成代码
//        codeGenerateService.generate(Collections.singletonList(currTemplate),
//                Collections.singletonList(this.tableInfo), this.generateOption,
//                new SqlParser(new StringBuffer()),
//                param);
//    }
}
