/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2025，所有权利保留。
 * <p>
 * 项目名：h6-codeGenerator
 * 文件名：TemplatePackageSufixs.java
 * 模块说明：
 * 修改历史：
 * 2025年05月07日 - zhangxuejun - 创建。
 */
package com.zxj.h6.codegenerator.service.impl.template;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangxuejun
 */
public class TemplatePackageSufixs {

  /** 默认值 */
  public static final Map<String, String> sufixs = new HashMap<String, String>();

  public static final String BASE_SUFIX = "bill/";

  static {
    sufixs.put(TemplateType.BillTemplateA.getModuleId(), BASE_SUFIX + "t1");
  }

}    