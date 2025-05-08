package com.zxj.h6.codegenerator.service;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.zxj.h6.codegenerator.entity.SettingsStorageDTO;
import com.zxj.h6.codegenerator.service.impl.SettingsStorageServiceImpl;

/**
 * @author zhangxuejun
 */
public interface SettingsStorageService extends PersistentStateComponent<SettingsStorageDTO> {
    /**
     * 获取实例
     *
     * @return {@link SettingsStorageService}
     */
    static SettingsStorageService getInstance() {
        return ServiceManager.getService(SettingsStorageServiceImpl.class);
    }

    /**
     * 获取设置存储
     *
     * @return {@link SettingsStorageDTO}
     */
    static SettingsStorageDTO getSettingsStorage() {
        return getInstance().getState();
    }
}
