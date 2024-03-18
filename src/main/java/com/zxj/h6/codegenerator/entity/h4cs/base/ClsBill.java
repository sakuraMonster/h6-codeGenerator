/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	hdpos4-commons-biz
 * 文件名：	ClsBill.java
 * 模块说明：	
 * 修改历史：
 * 2016年2月1日 - chenwenfeng - 创建。
 */
package com.zxj.h6.codegenerator.entity.h4cs.base;

/**
 * @author panzhibin
 *
 */
public class ClsBill extends BaseBill {
  private static final long serialVersionUID = 4233927490423646031L;

  private String cls;
  
  /** 类型 */
  public String getCls() {
    return cls;
  }

  public void setCls(String cls) {
    this.cls = cls;
  }
}
