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
 * 单据明细基类。
 * 
 * @author huangjunxian
 *
 */
public abstract class BasicBillLine extends Entity implements IsBillLine {
  private static final long serialVersionUID = -149569099983878345L;

  private String num;
  private Integer line;
  private String note;

  /** 单号 */
  public String getNum() {
    return num;
  }

  public void setNum(String num) {
    this.num = num;
  }

  /** 行号 */
  public Integer getLine() {
    return line;
  }

  public void setLine(Integer line) {
    this.line = line;
  }

  /** 备注 */
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

}
