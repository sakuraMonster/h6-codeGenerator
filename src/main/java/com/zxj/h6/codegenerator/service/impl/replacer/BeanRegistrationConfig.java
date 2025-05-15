/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2025，所有权利保留。
 * <p>
 * 项目名：h6-codeGenerator
 * 文件名：BeanRegistrationConfig.java
 * 模块说明：
 * 修改历史：
 * 2025年05月08日 - zhangxuejun - 创建。
 */
package com.zxj.h6.codegenerator.service.impl.replacer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangxuejun
 */
public class BeanRegistrationConfig {

  public static final String NEW_BEAN_DEFAULT_ID = "###";

  private final String targetFile;        // 目标注册文件路径
  private final String insertPattern;     // 插入位置的匹配模式
  private final Map<String, String> beanTemplates;      // Bean注册模板
  private final boolean insertBefore;     // 是否在匹配位置之前插入
  private final BeanFormatType formatType;      // 是否是XML格式
  private final String mapPropertyName;   // map属性名称（用于在特定map中插入entry）

  public BeanRegistrationConfig(String targetFile, String insertPattern,  String beanTemplate, boolean insertBefore, BeanFormatType formatType) {
    this(targetFile, insertPattern, buildBeanTemplates(beanTemplate), insertBefore, formatType, null);
  }

  public BeanRegistrationConfig(String targetFile, String insertPattern,  String beanTemplate, boolean insertBefore, BeanFormatType formatType, String mapPropertyName) {
    this(targetFile, insertPattern, buildBeanTemplates(beanTemplate), insertBefore, formatType, mapPropertyName);
  }

  public BeanRegistrationConfig(String targetFile, String insertPattern,  Map<String, String> beanTemplates, boolean insertBefore, BeanFormatType formatType) {
    this(targetFile, insertPattern, beanTemplates, insertBefore, formatType, null);
  }

  public BeanRegistrationConfig(String targetFile, String insertPattern,  Map<String, String> beanTemplates, boolean insertBefore, BeanFormatType formatType, String mapPropertyName) {
    this.targetFile = targetFile;
    this.insertPattern = insertPattern;
    this.beanTemplates = beanTemplates;
    this.insertBefore = insertBefore;
    this.formatType = formatType;
    this.mapPropertyName = mapPropertyName;
  }

  public static Map<String, String> buildBeanTemplates(String beanTemplate) {
    Map<String, String> beanTemplates = new HashMap<>();
    beanTemplates.put(NEW_BEAN_DEFAULT_ID, beanTemplate);

    return beanTemplates;
  }

  public String getTargetFile() {
    return targetFile;
  }

  public String getInsertPattern() {
    return insertPattern;
  }

  public  Map<String, String> getBeanTemplates() {
    return beanTemplates;
  }

  public boolean isInsertBefore() {
    return insertBefore;
  }

  public BeanFormatType getFormatType() {
    return formatType;
  }

  public String getMapPropertyName() {
    return mapPropertyName;
  }
}