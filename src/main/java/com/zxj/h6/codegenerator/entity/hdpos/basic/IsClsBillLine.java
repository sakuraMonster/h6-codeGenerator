/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	hdpos4-commons-biz
 * 文件名：	IsClsBillLine.java
 * 模块说明：	
 * 修改历史：
 * 2016年2月1日 - chenwenfeng - 创建。
 */
package com.zxj.h6.codegenerator.entity.hdpos.basic;

/**
 * @author chenwenfeng
 *
 */
public interface IsClsBillLine extends IsBillLine {
  /** 类型 */
  String getCls();

  void setCls(String cls);
}
