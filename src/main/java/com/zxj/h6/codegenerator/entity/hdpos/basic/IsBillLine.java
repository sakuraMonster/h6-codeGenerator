/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	scm-api
 * 文件名：	IsBillLine.java
 * 模块说明：	
 * 修改历史：
 * 2015年6月30日 - huangjunxian- 创建。
 */
package com.zxj.h6.codegenerator.entity.hdpos.basic;

/**
 * 单据明细|接口
 * 
 * @author huangjunxian
 * 
 */
public interface IsBillLine {
  String getNum();

  void setNum(String num);

  Integer getLine();

  void setLine(Integer line);

  String getNote();

  void setNote(String note);
}
