package com.zxj.h6.codegenerator.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.zxj.h6.codegenerator.entity.SettingsStorageDTO;
import com.zxj.h6.codegenerator.service.SettingsStorageService;
import com.zxj.h6.codegenerator.tool.GlobalDict;
import com.zxj.h6.codegenerator.tool.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

/**
 * @author zhangxuejun
 */
public class MainSettingForm implements Configurable {
    private JLabel versionLabel;
    private JPanel mainPanel;
    private JTextField authorEditor;
    private JLabel authorTitle;

    /**
     * 子配置
     */
    private Configurable[] childConfigurableArray;

    public MainSettingForm() {
    }

    @Override
    public String getDisplayName() {
        return "H6 CodeGenerator Config";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return getDisplayName();
    }

    @Override
    public @Nullable JComponent createComponent() {
        // TODO 临时隐藏未开发完毕的UI组件
        // 初始图标
        this.authorTitle.setIcon(AllIcons.Actions.IntentionBulb);
        this.loadSettingsStore();
        return mainPanel;
    }

    @Override
    public boolean isModified() {
        if (!Objects.equals(this.authorEditor.getText(), getSettingsStorage().getAuthor())) {
            return true;
        }
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        String author = this.authorEditor.getText();
        if (StringUtils.isNullOrBlank(author)) {
            throw new ConfigurationException("作者名称不能为空");
        }
        getSettingsStorage().setAuthor(author);
    }

    private void loadSettingsStore() {
        this.loadSettingsStore(getSettingsStorage());
    }

    /**
     * 加载配置信息
     *
     * @param settingsStorage 配置信息
     */
    public void loadSettingsStore(SettingsStorageDTO settingsStorage) {
        this.versionLabel.setText(GlobalDict.VERSION);
        this.authorEditor.setText(settingsStorage.getAuthor());
    }

    SettingsStorageDTO getSettingsStorage() {
        return SettingsStorageService.getSettingsStorage();
    }
}
