/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2025，所有权利保留。
 * <p>
 * 项目名：h6-codeGenerator
 * 文件名：ProjectModuleType.java
 * 模块说明：
 * 修改历史：
 * 2025年05月06日 - zhangxuejun - 创建。
 */
package com.zxj.h6.codegenerator.service.impl.template;

/**
 * @author zhangxuejun
 */
public enum ProjectModuleType {

  Pro("pro", "采购"),
  Sale("sale", "销售"),
  Dist("dist", "配货"),
  Account("account", "对账"),
  Mdata("mdata", "基础资料"),
  Prom("prom", "促销"),
  Sys("sys", "系统"),
  Wms("wms", "物流"),
  Wrh("wrh", "仓库"),
  Template("template", "模版");

  private String code;
  private String name;

  ProjectModuleType(String code, String name) {
    this.code = code;
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public String encode() {
    return this.name + "[" + this.code + "]";
  }

  public static ProjectModuleType decode(String projectModule) {
    String name = projectModule.substring(0, projectModule.indexOf('['));
    return ProjectModuleType.valueOfByName(name);
  }

  public static ProjectModuleType valueOfByName(String name) {
    if (name == null) {
      return null;
    }

    for (ProjectModuleType projectModuleType : ProjectModuleType.values()) {
      if (projectModuleType.getName().equals(name)) {
        return projectModuleType;
      }
    }

    return null;
  }


}
