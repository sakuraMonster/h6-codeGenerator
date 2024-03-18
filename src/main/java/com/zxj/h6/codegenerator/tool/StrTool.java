/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2024，所有权利保留。
 * <p>
 * 项目名：monster-plugin
 * 文件名：StrTool.java
 * 模块说明：
 * 修改历史：
 * 2024年03月11日 - zhangxuejun - 创建。
 */
package com.zxj.h6.codegenerator.tool;

/**
 * @author zhangxuejun
 */
public class StrTool {

  private static volatile StrTool strTool;

  StrTool() {
  }

  public static StrTool getInstance() {
    if (strTool == null) {
      Class var0 = StrTool.class;
      synchronized(StrTool.class) {
        if (strTool == null) {
          strTool = new StrTool();
        }
      }
    }

    return strTool;
  }

  public String toLowerCase(String str) {
    return str.toLowerCase();
  }

  public String toUpperCase(String str) {
    return str.toUpperCase();
  }

  public String replaceAssignStr(String str, String assign) {
    return str.replace(assign, "");
  }

}    