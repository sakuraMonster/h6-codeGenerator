/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2024，所有权利保留。
 * <p>
 * 项目名：h6-codeGenerator
 * 文件名：GenerateOptions.java
 * 模块说明：
 * 修改历史：
 * 2024年03月19日 - zhangxuejun - 创建。
 */
package com.zxj.h6.codegenerator.option;

/**
 * @author zhangxuejun
 */
public class GenerateOption {

  /**
   * 实体类模式
   */
  private Boolean entityModel;
  /**
   * 统一配置
   */
  private Boolean unifiedConfig;
  /**
   * 重新格式化代码
   */
  private Boolean reFormat;
  /**
   * 提示选是
   */
  private Boolean titleSure;
  /**
   * 提示选否
   */
  private Boolean titleRefuse;

  GenerateOption(Boolean entityModel, Boolean unifiedConfig, Boolean reFormat, Boolean titleSure, Boolean titleRefuse) {
    this.entityModel = entityModel;
    this.unifiedConfig = unifiedConfig;
    this.reFormat = reFormat;
    this.titleSure = titleSure;
    this.titleRefuse = titleRefuse;
  }

  public static GenerateOptionBuilder builder() {
    return new GenerateOptionBuilder();
  }

  public Boolean getEntityModel() {
    return entityModel;
  }

  public void setEntityModel(Boolean entityModel) {
    this.entityModel = entityModel;
  }

  public Boolean getUnifiedConfig() {
    return unifiedConfig;
  }

  public void setUnifiedConfig(Boolean unifiedConfig) {
    this.unifiedConfig = unifiedConfig;
  }

  public Boolean getReFormat() {
    return reFormat;
  }

  public void setReFormat(Boolean reFormat) {
    this.reFormat = reFormat;
  }

  public Boolean getTitleSure() {
    return titleSure;
  }

  public void setTitleSure(Boolean titleSure) {
    this.titleSure = titleSure;
  }

  public Boolean getTitleRefuse() {
    return titleRefuse;
  }

  public void setTitleRefuse(Boolean titleRefuse) {
    this.titleRefuse = titleRefuse;
  }

  public static class GenerateOptionBuilder {
    private Boolean entityModel;
    private Boolean unifiedConfig;
    private Boolean reFormat;
    private Boolean titleSure;
    private Boolean titleRefuse;

    GenerateOptionBuilder() {
    }

    public GenerateOptionBuilder entityModel(Boolean entityModel) {
      this.entityModel = entityModel;
      return this;
    }

    public GenerateOptionBuilder unifiedConfig(Boolean unifiedConfig) {
      this.unifiedConfig = unifiedConfig;
      return this;
    }

    public GenerateOptionBuilder reFormat(Boolean reFormat) {
      this.reFormat = reFormat;
      return this;
    }

    public GenerateOptionBuilder titleSure(Boolean titleSure) {
      this.titleSure = titleSure;
      return this;
    }

    public GenerateOptionBuilder titleRefuse(Boolean titleRefuse) {
      this.titleRefuse = titleRefuse;
      return this;
    }

    public GenerateOption build() {
      return new GenerateOption(this.entityModel, this.unifiedConfig, this.reFormat, this.titleSure, this.titleRefuse);
    }

    public String toString() {
      return "GenerateOptions.GenerateOptionsBuilder(entityModel=" + this.entityModel + ", unifiedConfig=" + this.unifiedConfig + ", reFormat=" + this.reFormat + ", titleSure=" + this.titleSure + ", titleRefuse=" + this.titleRefuse + ")";
    }
  }
}