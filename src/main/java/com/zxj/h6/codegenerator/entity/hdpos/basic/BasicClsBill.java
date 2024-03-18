/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2018，所有权利保留。
 * 
 * 项目名：	hdpos4-commons-jpa
 * 文件名：	PBasicBill.java
 * 模块说明：	
 * 修改历史：
 * 2018年11月21日 - huangjunxian - 创建。
 */
package com.zxj.h6.codegenerator.entity.hdpos.basic;

/**
 * 单据对象基类（带类型）。
 * 
 * @author huangjunxian
 *
 */
public abstract class BasicClsBill extends BasicBill {
  private static final long serialVersionUID = 2725819674024066595L;

  private String cls;

  /** 类型 */
  public String getCls() {
    return cls;
  }

  public void setCls(String cls) {
    this.cls = cls;
  }

}
