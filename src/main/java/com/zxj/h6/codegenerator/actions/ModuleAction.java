/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2025，所有权利保留。
 * <p>
 * 项目名：h6-codeGenerator
 * 文件名：ModuleAction.java
 * 模块说明：
 * 修改历史：
 * 2025年04月16日 - zhangxuejun - 创建。
 */
package com.zxj.h6.codegenerator.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.zxj.h6.codegenerator.ui.ModuleSelectPath;
import org.jetbrains.annotations.NotNull;

/**
 * @author zhangxuejun
 */
public class ModuleAction extends AnAction {
  @Override
  public void actionPerformed(@NotNull AnActionEvent event) {
    Project project = event.getProject();
    if (project == null) {
      return;
    }

    //开始处理
    new ModuleSelectPath(event.getProject()).show();
  }
}