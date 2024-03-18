/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2024，所有权利保留。
 * <p>
 * 项目名：monster-plugin
 * 文件名：StringUtils.java
 * 模块说明：
 * 修改历史：
 * 2024年03月05日 - zhangxuejun - 创建。
 */
package com.zxj.h6.codegenerator.tool;

/**
 * @author zhangxuejun
 */
public class StringUtils {

  public static boolean isNullOrBlank(String str) {
    return str == null || "".equals(str.trim());
  }

  public static Integer toInteger(String str) throws NumberFormatException {
    if (isNullOrBlank(str)) {
      return null;
    } else {
      String s = org.apache.commons.lang3.StringUtils.replace(str.trim(), ",", "");
      return Integer.valueOf(s);
    }
  }
}    