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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 单据对象基类。
 * 
 * @author huangjunxian
 *
 */
public abstract class BasicBill extends Basic implements IsBill {
  private static final long serialVersionUID = -3020137116536201949L;

  private String num;
  private Integer settleNo;
  private Integer recCnt;
  private Date prnTime;

  private Map<String, String> customFields = new HashMap<String, String>();
  
  /** 单号 */
  public String getNum() {
    return num;
  }

  public void setNum(String num) {
    this.num = num;
  }

  /** 期号 */
  public Integer getSettleNo() {
    return settleNo;
  }

  public void setSettleNo(Integer settleNo) {
    this.settleNo = settleNo;
  }

  /** 明细数 */
  public Integer getRecCnt() {
    return recCnt;
  }

  public void setRecCnt(Integer recCnt) {
    this.recCnt = recCnt;
  }

  /** 打印时间 */
  public Date getPrnTime() {
    return prnTime;
  }

  public void setPrnTime(Date prnTime) {
    this.prnTime = prnTime;
  }

  public Map<String, String> getCustomFields() {
    return customFields;
  }

  public void setCustomFields(Map<String, String> customFields) {
    this.customFields = customFields;
  }

}
