/** 
* 版权所有(C)，liuguilin，2017，所有权利保留。 
* 
* 项目名：	h4cs-api 
* 文件名：	BaseBill.java 
* 模块说明：	 
* 修改历史： 
* 2017年7月31日 - liuguilin - 创建。 
*/
package com.zxj.h6.codegenerator.entity.h4cs.base;



import com.zxj.h6.codegenerator.entity.hdpos.basic.Entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuguilin
 * 
 */
public class BaseBill extends Entity {

  private static final long serialVersionUID = -2397365631279937602L;

  private String num;
  private long version = -1L;
  private Integer settleNo;
  private Date filDate;
  private String filler;
  private Date lstModified;
  private String lstModifier;
  private String note;
  private Integer stat;
  private Integer recCnt = 0;
  private Integer printCount = 0;
  private Date printTime;
  private String printer;

  private Map<String, String> customFields = new HashMap<String, String>();

  /** 单号 */
  public String getNum() {
    return num;
  }

  public void setNum(String num) {
    this.num = num;
  }

  /** 版本号 */
  public long getVersion() {
    return version;
  }

  public void setVersion(long version) {
    this.version = version;
  }

  /** 期号 */
  public Integer getSettleNo() {
    return settleNo;
  }

  public void setSettleNo(Integer settleNo) {
    this.settleNo = settleNo;
  }

  /** 创建时间 */
  public Date getFilDate() {
    return filDate;
  }

  public void setFilDate(Date filDate) {
    this.filDate = filDate;
  }

  /** 最后修改时间 */
  public Date getLstModified() {
    return lstModified;
  }

  public void setLstModified(Date lstModified) {
    this.lstModified = lstModified;
  }

  /** 备注 */
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  /** 状态 */
  public Integer getStat() {
    return stat;
  }

  public void setStat(Integer stat) {
    this.stat = stat;
  }

  /** 明细数 */
  public Integer getRecCnt() {
    return recCnt;
  }

  public void setRecCnt(Integer recCnt) {
    this.recCnt = recCnt;
  }

  /** 打印次数 */
  public Integer getPrintCount() {
    return printCount;
  }

  public void setPrintCount(Integer printCount) {
    this.printCount = printCount;
  }

  /** 最后打印时间 */
  public Date getPrintTime() {
    return printTime;
  }

  public void setPrintTime(Date printTime) {
    this.printTime = printTime;
  }

  /** 创建人 */
  public String getFiller() {
    return filler;
  }

  public void setFiller(String filler) {
    this.filler = filler;
  }

  /** 最后修改人 */
  public String getLstModifier() {
    return lstModifier;
  }

  public void setLstModifier(String lstModifier) {
    this.lstModifier = lstModifier;
  }

  /** 最后打印人 */
  public String getPrinter() {
    return printer;
  }

  public void setPrinter(String printer) {
    this.printer = printer;
  }

  /** 自定义字段 */
  public Map<String, String> getCustomFields() {
    return customFields;
  }

  public void setCustomFields(Map<String, String> customFields) {
    this.customFields = customFields;
  }

}
