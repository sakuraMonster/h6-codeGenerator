/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2024，所有权利保留。
 * <p>
 * 项目名：monster-plugin
 * 文件名：MainActionGroup.java
 * 模块说明：
 * 修改历史：
 * 2024年03月04日 - zhangxuejun - 创建。
 */
package com.zxj.h6.codegenerator.actions;

import com.intellij.database.psi.DbTable;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.sjhy.plugin.dict.GlobalDict;
import com.sjhy.plugin.service.TableInfoSettingsService;
import com.sjhy.plugin.tool.CacheDataUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangxuejun
 */
public class MainActionGroup extends ActionGroup {

  /**
   * 缓存数据工具类
   */
  private CacheDataUtils cacheDataUtils = CacheDataUtils.getInstance();

  /**
   * 是否不存在子菜单
   */
  private boolean notExistsChildren;

  /**
   *  根据右键在不同的选项上展示不同的子菜单
   * @param event
   * @return
   */
  @Override
  public AnAction @NotNull [] getChildren(@Nullable AnActionEvent event) {
    // 获取当前项目
    Project project = getEventProject(event);
    if (project == null) {
      return getEmptyAnAction();
    }

    //获取选中的PSI元素
    PsiElement psiElement = event.getData(LangDataKeys.PSI_ELEMENT);
    DbTable selectDbTable = null;
    if (psiElement instanceof DbTable) {
      selectDbTable = (DbTable) psiElement;
    }
    if (selectDbTable == null) {
      return getEmptyAnAction();
    }
    //获取选中的所有表
    PsiElement[] psiElements = event.getData(LangDataKeys.PSI_ELEMENT_ARRAY);
    if (psiElements == null || psiElements.length == 0) {
      return getEmptyAnAction();
    }
    List<DbTable> dbTableList = new ArrayList<>();
    for (PsiElement element : psiElements) {
      if (!(element instanceof DbTable)) {
        continue;
      }
      DbTable dbTable = (DbTable) element;
      dbTableList.add(dbTable);
    }
    if (dbTableList.isEmpty()) {
      return getEmptyAnAction();
    }

    //保存数据到缓存
    cacheDataUtils.setDbTableList(dbTableList);
    cacheDataUtils.setSelectDbTable(selectDbTable);
    this.notExistsChildren = false;
    return getMenuList();
  }

  /**
   * 初始化注册子菜单项目
   *
   * @return 子菜单数组
   */
  private AnAction[] getMenuList() {
    String mainActionId = "com.zxj.easy.code.action.generate";
    String configActionId = "com.zxj.easy.code.action.config";
    ActionManager actionManager = ActionManager.getInstance();
    // 代码生成菜单
    AnAction mainAction = actionManager.getAction(mainActionId);
    if (mainAction == null) {
      mainAction = new MainAction("Generate Code");
      actionManager.registerAction(mainActionId, mainAction);
    }
    // 表配置菜单
    AnAction configAction = actionManager.getAction(configActionId);
    if (configAction == null) {
      configAction = new ConfigAction("Config Table");
      actionManager.registerAction(configActionId, configAction);
    }
    AnAction clearConfigAction = new AnAction("Clear Config") {
      @Override
      public void actionPerformed(@NotNull AnActionEvent e) {
        DbTable dbTable = CacheDataUtils.getInstance().getSelectDbTable();
        if (dbTable == null) {
          return;
        }
        TableInfoSettingsService.getInstance().removeTableInfo(dbTable);
        Messages.showInfoMessage(dbTable.getName() + "表配置信息已重置成功", GlobalDict.TITLE_INFO);
      }
    };
    // 返回所有菜单
    return new AnAction[]{mainAction, configAction, clearConfigAction};
  }

  /**
   * 获取空菜单组
   *
   * @return 空菜单组
   */
  private AnAction[] getEmptyAnAction() {
    this.notExistsChildren = true;
    return AnAction.EMPTY_ARRAY;
  }
}