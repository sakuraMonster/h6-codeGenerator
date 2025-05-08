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
import com.zxj.h6.codegenerator.service.impl.replacer.FileProcessor;
import com.zxj.h6.codegenerator.service.impl.replacer.ModuleConfig;
import com.zxj.h6.codegenerator.service.impl.replacer.PathConstants;
import com.zxj.h6.codegenerator.service.impl.template.PackageType;
import com.zxj.h6.codegenerator.service.impl.template.TemplatePackageSufixs;
import com.zxj.h6.codegenerator.tool.ClassBasedGenerator;
import com.zxj.h6.codegenerator.service.impl.template.ProjectModuleType;
import com.zxj.h6.codegenerator.service.impl.template.TemplateType;

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
          String moduleId, Integer moduleNo) {

    logger.info("Starting module replacement...");

    ModuleConfig config = new ModuleConfig(templateType.getModuleId(), templateType.getModuleNo(),
            moduleId, moduleNo, projectModuleType.getCode());

    String basePath = project.getBasePath();

    try {
      for (PackageType packageType : PackageType.values()) {
        String sourceDir = basePath + "/" + TEMP_HOLDER_CHAR + "/" + PathConstants.BASE_PACKAGE
                + "/" + PathConstants.BASE_TEMPLATE_MODULE
                + "/" + packageType.name() + "/" + TemplatePackageSufixs.sufixs.get(templateType.getModuleId());
        String targetDir = basePath + "/" + TEMP_HOLDER_CHAR + "/" + PathConstants.BASE_PACKAGE
                + "/" + projectModuleType.getCode() + "/" + packageType.name() + "/" + moduleId.toLowerCase();
        switch (packageType) {
        case service:
          sourceDir = sourceDir.replace(TEMP_HOLDER_CHAR, PathConstants.BASE_TEMPLATE_API);
          targetDir = targetDir.replace(TEMP_HOLDER_CHAR,
                  PathConstants.BASE_TEMPLATE_API.replace(PathConstants.BASE_TEMPLATE_MODULE,
                          projectModuleType.getCode()));
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
                  + "/" + TemplatePackageSufixs.sufixs.get(templateType.getModuleId());
          targetDir = basePath + "/" + PathConstants.BASE_WEB + "/" + PathConstants.BASE_PACKAGE + "/" + packageType.name()
                  + "/" + projectModuleType.getCode()
                  + "/" + moduleId.toLowerCase();
          break;
        case extStore:
          sourceDir = basePath + "/" + PathConstants.BASE_EXT_MODULE + "/" + PathConstants.BASE_EXT_PATH
                  + "/" + PathConstants.BASE_TEMPLATE_MODULE
                  + "/" + PathConstants.BASE_EXT_STORE + "/" + TemplatePackageSufixs.sufixs.get(templateType.getModuleId());
          targetDir = basePath + "/" + PathConstants.BASE_EXT_MODULE + "/" + PathConstants.BASE_EXT_PATH
                  + "/" + projectModuleType.getCode() + "2"
                  + "/" + PathConstants.BASE_EXT_STORE + "/" + moduleId.toLowerCase();
          break;
        case extView:
          sourceDir = basePath + "/" + PathConstants.BASE_EXT_MODULE + "/" + PathConstants.BASE_EXT_PATH
                  + "/" + PathConstants.BASE_TEMPLATE_MODULE
                  + "/" + PathConstants.BASE_EXT_VIEW + "/" + TemplatePackageSufixs.sufixs.get(templateType.getModuleId());
          targetDir = basePath + "/" + PathConstants.BASE_EXT_MODULE + "/" + PathConstants.BASE_EXT_PATH
                  + "/" + projectModuleType.getCode() + "2"
                  + "/" + PathConstants.BASE_EXT_VIEW + "/" + moduleId.toLowerCase();
          break;
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