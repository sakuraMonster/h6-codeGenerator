/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2018，所有权利保留。
 * 
 * 项目名：	hdpos4-commons-jpa
 * 文件名：	PBasicBillLine.java
 * 模块说明：	
 * 修改历史：
 * 2018年11月21日 - huangjunxian - 创建。
 */
package com.zxj.h6.codegenerator.entity.hdpos.basic;

/**
 * 单据明细基类(带类型）。
 * 
 * @author huangjunxian
 *
 */
public abstract class BasicClsBillLine extends BasicBillLine {
  private static final long serialVersionUID = -4642371732571330410L;

  private String cls;

  /**单据类型*/
  public String getCls() {
    return cls;
  }

  public void setCls(String cls) {
    this.cls = cls;
  }

}
