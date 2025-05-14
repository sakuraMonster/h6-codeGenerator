/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2025，所有权利保留。
 * <p>
 * 项目名：h6-codeGenerator
 * 文件名：TemplateType.java
 * 模块说明：
 * 修改历史：
 * 2025年05月06日 - zhangxuejun - 创建。
 */
package com.zxj.h6.codegenerator.service.impl.template;

/**
 *    模版类型
 * @author zhangxuejun
 */
public enum TemplateType {



  BillTemplateA("billTemplateA", 999001, "单据模板A", TemplateConstants.BASE_PACKAGE_SUFIX + "t1"),

  BillTemplateB("billTemplateB", 999002, "单据模板B", TemplateConstants.BASE_PACKAGE_SUFIX + "t2");

  private String moduleId;
  private Integer moduleNo;
  private String name;
  private String packageSufix;

  public static final String BASE_PACKAGE_SUFIX = "bill/";

  TemplateType(String moduleId, Integer moduleNo, String name, String packageSufix) {
    this.moduleId = moduleId;
    this.moduleNo = moduleNo;
    this.name = name;
    this.packageSufix = packageSufix;
  }

  public String getModuleId() {
    return moduleId;
  }

  public Integer getModuleNo() {
    return moduleNo;
  }

  public String getName() {
    return name;
  }

  public String getPackageSufix() {
    return packageSufix;
  }

  public static TemplateType valueOfByName(String name) {
    for (TemplateType templateType : TemplateType.values()) {
      if (templateType.getName().equals(name)) {
        return templateType;
      }
    }
    return null;
  }

  public String encode() {
    return this.name + "[" + this.moduleId + "]";
  }

  public static TemplateType decode(String template) {
    String name = template.substring(0, template.indexOf('['));
    return TemplateType.valueOfByName(name);
  }
}
