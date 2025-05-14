/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2025，所有权利保留。
 * <p>
 * 项目名：h6-codeGenerator
 * 文件名：NewCodeGenerateServiceImpl.java
 * 模块说明：
 * 修改历史：
 * 2025年04月17日 - zhangxuejun - 创建。
 */
package com.zxj.h6.codegenerator.service.impl;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.zxj.h6.codegenerator.service.NewCodeGenerateService;
import com.zxj.h6.codegenerator.service.impl.replacer.BeanRegistrationConfig;
import com.zxj.h6.codegenerator.service.impl.replacer.FileProcessor;
import com.zxj.h6.codegenerator.service.impl.replacer.ModuleConfig;
import com.zxj.h6.codegenerator.service.impl.replacer.PathConstants;
import com.zxj.h6.codegenerator.service.impl.template.PackageType;
import com.zxj.h6.codegenerator.service.impl.template.TemplateConstants;
import com.zxj.h6.codegenerator.tool.ClassBasedGenerator;
import com.zxj.h6.codegenerator.service.impl.template.ProjectModuleType;
import com.zxj.h6.codegenerator.service.impl.template.TemplateType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangxuejun
 */
public class NewCodeGenerateServiceImpl implements NewCodeGenerateService {

  private static final String TEMP_HOLDER_CHAR = "###";

  private static final Logger logger = Logger.getInstance(NewCodeGenerateServiceImpl.class);

  /**
   * 项目对象
   */
  private Project project;
  /**
   * 模型管理
   */
  private ModuleManager moduleManager;

  public NewCodeGenerateServiceImpl(Project project) {
    this.project = project;
    this.moduleManager = ModuleManager.getInstance(project);
  }

  @Override
  public void generate(TemplateType templateType, ProjectModuleType projectModuleType,
          String moduleId, Integer moduleNo, String moduleName) {

    logger.info("Starting module replacement...");
    String basePath = project.getBasePath();

    ModuleConfig config = new ModuleConfig(templateType, moduleId, moduleNo, moduleName,
            projectModuleType.getCode());

    try {
      for (PackageType packageType : PackageType.values()) {
        // 先清除配置
        config.getBeanRegistrations().clear();
        String sourceDir = basePath + "/" + TEMP_HOLDER_CHAR + "/" + PathConstants.BASE_PACKAGE
                + "/" + PathConstants.BASE_TEMPLATE_MODULE
                + "/" + packageType.name() + "/" + templateType.getPackageSufix();
        String targetDir = basePath + "/" + TEMP_HOLDER_CHAR + "/" + PathConstants.BASE_PACKAGE
                + "/" + projectModuleType.getCode() + "/" + packageType.name() + "/" + moduleId.toLowerCase();
        switch (packageType) {
        case service:
          sourceDir = sourceDir.replace(TEMP_HOLDER_CHAR, PathConstants.BASE_TEMPLATE_API);
          targetDir = targetDir.replace(TEMP_HOLDER_CHAR,
                  PathConstants.BASE_TEMPLATE_API.replace(PathConstants.BASE_TEMPLATE_MODULE,
                          projectModuleType.getCode()));

          // 目标文件
          String targetServerXmlFile = basePath + "/" + PathConstants.BASE_TEMPLATE_CORE.replace(
                  PathConstants.BASE_TEMPLATE_MODULE,
                  projectModuleType.getCode()) + "/" + TemplateConstants.serverXmls.get(
                                                                                templateType.getModuleId())
                                                                                   .replace(
                                                                                           ProjectModuleType.Template.getCode(),
                                                                                           projectModuleType.getCode());
          String targetLogXmlFile = basePath + "/" + PathConstants.BASE_SYS_CORE + "/" + TemplateConstants.logXmls.get(
                                                                                templateType.getModuleId());

          String targetSysXmlFile = basePath + "/" + PathConstants.BASE_SYS_CORE + "/" + TemplateConstants.sysXmls.get(
                  templateType.getModuleId());

          String targetMsgXmlFile = basePath + "/" + PathConstants.BASE_SYS_CORE + "/" + TemplateConstants.msgXmls.get(
                  templateType.getModuleId());

          switch (templateType) {
            case BillTemplateA:
              BeanRegistrationConfig serverXmlConfig = new BeanRegistrationConfig(
                      targetServerXmlFile, "\\s*</beans>",
                      TemplateConstants.serverXmlContexts.get(templateType.getModuleId()), true, true);
              config.addBeanRegistration(serverXmlConfig);

              Map<String, String> logBeanTemplates = new HashMap<>();
              logBeanTemplates.put(TemplateConstants.LOG_BEANID, TemplateConstants.logXmlContexts.get(templateType.getModuleId()));
              BeanRegistrationConfig logXmlConfig = new BeanRegistrationConfig(
                      targetLogXmlFile, null,
                      logBeanTemplates, false, true, "tables");
              config.addBeanRegistration(logXmlConfig);

              Map<String, String> sysDqueryBeanTemplates = new HashMap<>();
              sysDqueryBeanTemplates.put(TemplateConstants.SYS_DQUERY_BEANID,
                      TemplateConstants.sysDqueryXmlContexts.get(templateType.getModuleId()));
              BeanRegistrationConfig sysDqueryXmlConfig = new BeanRegistrationConfig(
                      targetSysXmlFile, null,
                      sysDqueryBeanTemplates, false, true, "constClassNames");
              config.addBeanRegistration(sysDqueryXmlConfig);

              Map<String, String> msgBeanTemplates = new HashMap<>();
              msgBeanTemplates.put(TemplateConstants.CLIENT_MESSSAGE_BEANID, TemplateConstants.msgXmlClientContexts.get(templateType.getModuleId()));
              msgBeanTemplates.put(TemplateConstants.SERVER_MESSSAGE_BEANID, TemplateConstants.msgXmlServerContexts.get(templateType.getModuleId()));
              msgBeanTemplates.put(TemplateConstants.COMBO_MESSSAGE_BEANID, TemplateConstants.msgXmlComboContexts.get(templateType.getModuleId()));
              BeanRegistrationConfig msgXmlConfig = new BeanRegistrationConfig(
                      targetMsgXmlFile, null,
                      msgBeanTemplates, false, true, "moduleMap");
              config.addBeanRegistration(msgXmlConfig);
              break;
            default:
              break;
            }

          break;
        case dao:
        case impl:
          sourceDir = sourceDir.replace(TEMP_HOLDER_CHAR, PathConstants.BASE_TEMPLATE_CORE);
          targetDir = targetDir.replace(TEMP_HOLDER_CHAR,
                  PathConstants.BASE_TEMPLATE_CORE.replace(PathConstants.BASE_TEMPLATE_MODULE,
                          projectModuleType.getCode()));
          break;
        case controllers:
          sourceDir = basePath + "/" + PathConstants.BASE_WEB + "/" + PathConstants.BASE_PACKAGE + "/" + packageType.name()
                  + "/" + PathConstants.BASE_TEMPLATE_MODULE
                  + "/" + templateType.getPackageSufix();
          targetDir = basePath + "/" + PathConstants.BASE_WEB + "/" + PathConstants.BASE_PACKAGE + "/" + packageType.name()
                  + "/" + projectModuleType.getCode()
                  + "/" + moduleId.toLowerCase();
          break;
        case extStore:
          sourceDir = basePath + "/" + PathConstants.BASE_EXT_MODULE + "/" + PathConstants.BASE_EXT_PATH
                  + "/" + PathConstants.BASE_TEMPLATE_MODULE
                  + "/" + PathConstants.BASE_EXT_STORE + "/" + templateType.getPackageSufix();
          targetDir = basePath + "/" + PathConstants.BASE_EXT_MODULE + "/" + PathConstants.BASE_EXT_PATH
                  + "/" + projectModuleType.getCode() + "2"
                  + "/" + PathConstants.BASE_EXT_STORE + "/" + moduleId.toLowerCase();
          break;
        case extView:
          sourceDir = basePath + "/" + PathConstants.BASE_EXT_MODULE + "/" + PathConstants.BASE_EXT_PATH
                  + "/" + PathConstants.BASE_TEMPLATE_MODULE
                  + "/" + PathConstants.BASE_EXT_VIEW + "/" + templateType.getPackageSufix();
          targetDir = basePath + "/" + PathConstants.BASE_EXT_MODULE + "/" + PathConstants.BASE_EXT_PATH
                  + "/" + projectModuleType.getCode() + "2"
                  + "/" + PathConstants.BASE_EXT_VIEW + "/" + moduleId.toLowerCase();
          break;
        case cp:
          sourceDir = basePath + "/" + PathConstants.BASE_TEMPLATE_CORE + "/" + PathConstants.BASE_CP_PROPERTIES_PATH
                   + "/" + PathConstants.BASE_TEMPLATE_MODULE + "/dao"
                   + "/" + templateType.getPackageSufix();
          targetDir = basePath + "/" + PathConstants.BASE_TEMPLATE_CORE.replace(PathConstants.BASE_TEMPLATE_MODULE, projectModuleType.getCode())+ "/" + PathConstants.BASE_CP_PROPERTIES_PATH
                  + "/" + projectModuleType.getCode() + "/dao"
                  + "/" + moduleId.toLowerCase();
        default:
          break;
        }
        // 执行文件处理
        FileProcessor.of(config, sourceDir, targetDir).processFiles();
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }



    logger.info("Module replacement completed successfully!");
  }

  /**
   * 基于模板类生成代码
   *
   * @param templateClassName 模板类的全限定名
   * @param targetPackage 目标包名
   * @param targetClassName 目标类名
   * @param sourcePattern 源模式（例如："BillTemplateA"）
   * @param targetPattern 目标模式（例如："BillTemplateB"）
   * @return 生成的代码
   */
  public String generateFromClass(String templateClassName, String targetPackage, String targetClassName,
          String sourcePattern, String targetPattern) {
    try {
      ClassBasedGenerator generator = new ClassBasedGenerator(project, templateClassName, sourcePattern, targetPattern);
      return generator.generateCode(targetPackage, targetClassName);
    } catch (Exception e) {
      Messages.showErrorDialog(project, "Failed to generate code from class: " + e.getMessage(), "Error");
      return null;
    }
  }

  /**
   * 基于模板类生成代码（不进行模式替换）
   */
  public String generateFromClass(String templateClassName, String targetPackage, String targetClassName) {
    return generateFromClass(templateClassName, targetPackage, targetClassName, null, null);
  }
}    