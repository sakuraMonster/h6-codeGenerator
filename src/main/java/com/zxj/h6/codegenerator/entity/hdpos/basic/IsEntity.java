/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	scm-api
 * 文件名：	IsEntity.java
 * 模块说明：	
 * 修改历史：
 * 2015年7月21日 - chenwenfeng - 创建。
 */
package com.zxj.h6.codegenerator.entity.hdpos.basic;

/**
 * @author chenwenfeng
 * 
 */
public interface IsEntity {

  String getUuid();

  void setUuid(String uuid) throws UnsupportedOperationException;
}
