package com.zxj.h6.codegenerator.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.sjhy.plugin.ui.ConfigTableDialog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 表配置菜单
 *
 * @author zhangxuejun
 */
public class ConfigAction extends AnAction {
    /**
     * 构造方法
     *
     * @param text 菜单名称
     */
    ConfigAction(@Nullable String text) {
        super(text);
    }

    /**
     * 处理方法
     *
     * @param event 事件对象
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            return;
        }
        new ConfigTableDialog().show();
    }
}
