/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2025，所有权利保留。
 * <p>
 * 项目名：h6-codeGenerator
 * 文件名：PackageConfig.java
 * 模块说明：
 * 修改历史：
 * 2025年05月06日 - zhangxuejun - 创建。
 */
package com.zxj.h6.codegenerator.service.impl.template;

/**
 * @author zhangxuejun
 */
public class PackageConfig {


  private String prefix;    // 包名前缀
  private PackageType packageType;    //  包类型
  private String moduleName;    // 模块名
  private String suffix;    // 包名后缀

  public PackageConfig(String prefix, PackageType packageType, String moduleName, String suffix) {
    this.prefix = prefix;
    this.packageType = packageType;
    this.moduleName = moduleName;
    this.suffix = suffix;
  }


}    