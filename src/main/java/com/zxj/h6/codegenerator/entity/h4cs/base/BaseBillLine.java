/** 
* 版权所有(C)，liuguilin，2017，所有权利保留。 
* 
* 项目名：	h4cs-api 
* 文件名：	BaseBillLine.java 
* 模块说明：	 
* 修改历史： 
* 2017年8月16日 - liuguilin - 创建。 
*/
package com.zxj.h6.codegenerator.entity.h4cs.base;

import com.zxj.h6.codegenerator.entity.hdpos.basic.Entity;

/**
 * @author liuguilin
 * 
 */
public class BaseBillLine extends Entity {
  private static final long serialVersionUID = -2833353568933450078L;

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
