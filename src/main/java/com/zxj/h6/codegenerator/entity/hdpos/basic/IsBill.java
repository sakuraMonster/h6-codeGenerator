/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	scm-api
 * 文件名：	IsBill.java
 * 模块说明：	
 * 修改历史：
 * 2015年7月21日 - chenwenfeng - 创建。
 */
package com.zxj.h6.codegenerator.entity.hdpos.basic;

import java.util.Date;

/**
 * @author chenwenfeng
 * 
 */
public interface IsBill {

  String getNum();

  void setNum(String num);

  /** 期号 */
  Integer getSettleNo();

  void setSettleNo(Integer settleNo);

  /** 备注 */
  String getNote();

  void setNote(String note);

  /** 明细数 */
  Integer getRecCnt();

  void setRecCnt(Integer recCnt);

  /** 填单时间 */
  Date getFilDate();

  void setFilDate(Date filDate);

  /** 填单人 */
  String getFiller();

  void setFiller(String filler);

  /** 最后修改时间 */
  Date getLstupdTime();

  void setLstupdTime(Date lstupdTime);

  /** 打印时间 */
  Date getPrnTime();

  void setPrnTime(Date prnTime);
}
