/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2024，所有权利保留。
 * <p>
 * 项目名：monster-plugin
 * 文件名：InitMainActionGroupListener.java
 * 模块说明：
 * 修改历史：
 * 2024年03月04日 - zhangxuejun - 创建。
 */
package com.zxj.h6.codegenerator.listener;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.application.ApplicationActivationListener;
import com.intellij.openapi.wm.IdeFrame;
import org.jetbrains.annotations.NotNull;

/**
 * @author zhangxuejun
 */
public class InitMainActionGroupListener implements ApplicationActivationListener {

  public void applicationActivated(@NotNull IdeFrame ideFrame) {
    ActionManager actionManager = ActionManager.getInstance();
    AnAction srcAction = actionManager.getAction("con.sjhy.easy.code.action");
    if(srcAction != null) {
      actionManager.unregisterAction("con.sjhy.easy.code.action");
    }
  }
}