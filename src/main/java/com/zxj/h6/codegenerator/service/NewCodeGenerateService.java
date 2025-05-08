/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2025，所有权利保留。
 * <p>
 * 项目名：h6-codeGenerator
 * 文件名：NewCodeGenerateService.java
 * 模块说明：
 * 修改历史：
 * 2025年04月17日 - zhangxuejun - 创建。
 */
package com.zxj.h6.codegenerator.service;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.zxj.h6.codegenerator.service.impl.template.ProjectModuleType;
import com.zxj.h6.codegenerator.service.impl.template.TemplateType;
import org.jetbrains.annotations.NotNull;

/**
 * @author zhangxuejun
 */
public interface NewCodeGenerateService {

  /**
   * 获取实例对象
   *
   * @param project 项目对象
   * @return 实例对象
   */
  static NewCodeGenerateService getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, NewCodeGenerateService.class);
  }

  String generateFromClass(String templateClassName, String targetPackage, String targetClassName,
          String sourcePattern, String targetPattern);

  void generate(TemplateType templateType, ProjectModuleType projectModuleType, String moduleId, Integer moduleNo);
}    