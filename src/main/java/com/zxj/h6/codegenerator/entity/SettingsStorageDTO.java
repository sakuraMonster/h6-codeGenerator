/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2025，所有权利保留。
 * <p>
 * 项目名：h6-codeGenerator
 * 文件名：SettingsStorageDTO.java
 * 模块说明：
 * 修改历史：
 * 2025年05月07日 - zhangxuejun - 创建。
 */
package com.zxj.h6.codegenerator.entity;

import com.intellij.ide.fileTemplates.impl.UrlUtil;
import com.intellij.util.ExceptionUtil;
import com.zxj.h6.codegenerator.tool.GlobalDict;
import com.zxj.h6.codegenerator.tool.JSON;

import java.util.Arrays;

/**
 * @author zhangxuejun
 */
public class SettingsStorageDTO {

  /**
   * 作者
   */
  private String author;

  /**
   * 版本号
   */
  private String version;

  /**
   * 返回默认值，不使用静态常量，防止默认值别篡改
   *
   * @return 储存对象
   */
  public static SettingsStorageDTO defaultVal() {
    try {
      // 从配置文件中加载配置
      String json = UrlUtil.loadText(SettingsStorageDTO.class.getResource("/defaultConfig.json"));
      return JSON.parse(json, SettingsStorageDTO.class);
    } catch (Exception e) {
      ExceptionUtil.rethrow(e);
    }
    // 配置文件加载失败，直接创建配置
    SettingsStorageDTO storage = new SettingsStorageDTO();
    storage.author = GlobalDict.AUTHOR;
    storage.version = GlobalDict.VERSION;
    return storage;
  }


  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }
}