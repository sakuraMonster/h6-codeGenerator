/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2018，所有权利保留。
 * 
 * 项目名：	hdpos4-commons-jpa
 * 文件名：	PBasicData.java
 * 模块说明：	
 * 修改历史：
 * 2018年11月21日 - huangjunxian - 创建。
 */
package com.zxj.h6.codegenerator.entity.hdpos.basic;

import java.util.Date;

/**
 * 对象基类。
 * 
 * @author huangjunxian
 *
 */
public abstract class Basic extends Entity {
  private static final long serialVersionUID = -7170734944364216342L;

  private long version;
  private String note;
  private Date filDate;
  private String filler;
  private Date lstupdTime;
  private String lastModifyOper;

  /** 版本号 */
  public long getVersion() {
    return version;
  }

  public void setVersion(long version) {
    this.version = version;
  }

  /** 备注 */
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  /** 创建时间 */
  public Date getFilDate() {
    return filDate;
  }

  public void setFilDate(Date filDate) {
    this.filDate = filDate;
  }

  /** 创建人 */
  public String getFiller() {
    return filler;
  }

  public void setFiller(String filler) {
    this.filler = filler;
  }

  /** 最后修改时间 */
  public Date getLstupdTime() {
    return lstupdTime;
  }

  public void setLstupdTime(Date lstupdTime) {
    this.lstupdTime = lstupdTime;
  }

  /** 最后修改人 */
  public String getLastModifyOper() {
    return lastModifyOper;
  }

  public void setLastModifyOper(String lastModifyOper) {
    this.lastModifyOper = lastModifyOper;
  }

}
