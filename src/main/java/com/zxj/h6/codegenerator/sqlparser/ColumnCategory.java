package com.zxj.h6.codegenerator.sqlparser;

/**
 * 字段结构
 *
 * @author zhangxuejun
 */
public class ColumnCategory {
  private String column;
  private String type;
  private String constant;
  private String comment;

  /** 字段名 */
  public String getColumn() {
    return column;
  }

  public void setColumn(String column) {
    this.column = column;
  }

  /** 字段类型 */
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  /** 约束 */
  public String getConstant() {
    return constant;
  }

  public void setConstant(String constant) {
    this.constant = constant;
  }

  /** 注释 */
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
}