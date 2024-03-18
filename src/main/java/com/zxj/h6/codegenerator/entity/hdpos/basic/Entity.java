/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2024，所有权利保留。
 * <p>
 * 项目名：monster-plugin
 * 文件名：Entity.java
 * 模块说明：
 * 修改历史：
 * 2024年03月08日 - zhangxuejun - 创建。
 */
package com.zxj.h6.codegenerator.entity.hdpos.basic;

import java.io.Serializable;

/**
 * 提供所有数据实体的基类，这是一个POJO类，符合JavaBean规范。
 *
 * @author lxm
 * @since 1.0
 *
 */
public abstract class Entity implements Serializable, IsEntity {

  private static final long serialVersionUID = 997723760796411734L;

  private String uuid;

  /**
   * 全局唯一标识。
   */
  @Override
  public String getUuid() {
    return uuid;
  }

  @Override
  public void setUuid(String uuid) throws UnsupportedOperationException {
    this.uuid = uuid;
  }

  @Override
  public String toString() {
    return uuid;
  }

}
