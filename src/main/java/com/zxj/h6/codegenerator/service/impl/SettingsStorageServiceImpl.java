/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2025，所有权利保留。
 * <p>
 * 项目名：h6-codeGenerator
 * 文件名：SettingsStorageServiceImpl.java
 * 模块说明：
 * 修改历史：
 * 2025年05月07日 - zhangxuejun - 创建。
 */
package com.zxj.h6.codegenerator.service.impl;

import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.zxj.h6.codegenerator.entity.SettingsStorageDTO;
import com.zxj.h6.codegenerator.service.SettingsStorageService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author zhangxuejun
 */
@State(name = "H6CodeGeneratorSetting", storages = @Storage("h6-code-generator-setting.xml"))
public class SettingsStorageServiceImpl implements SettingsStorageService {

  private SettingsStorageDTO settingsStorage = SettingsStorageDTO.defaultVal();

  @Override
  public @Nullable SettingsStorageDTO getState() {
    return settingsStorage;
  }

  @Override
  public void loadState(@NotNull SettingsStorageDTO state) {
    this.settingsStorage = state;
  }

}