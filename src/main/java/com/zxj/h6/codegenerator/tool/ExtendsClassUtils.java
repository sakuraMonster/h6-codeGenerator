/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2024，所有权利保留。
 * <p>
 * 项目名：monster-plugin
 * 文件名：ExtendsClassUtils.java
 * 模块说明：
 * 修改历史：
 * 2024年03月08日 - zhangxuejun - 创建。
 */
package com.zxj.h6.codegenerator.tool;

import com.sjhy.plugin.entity.TableInfo;
import com.zxj.h6.codegenerator.entity.h4cs.base.BaseBill;
import com.zxj.h6.codegenerator.entity.hdpos.basic.BasicClsBillLine;
import com.zxj.h6.codegenerator.entity.h4cs.base.BaseBillLine;
import com.zxj.h6.codegenerator.entity.h4cs.base.ClsBill;
import com.zxj.h6.codegenerator.entity.h4cs.base.ClsBillLine;
import com.zxj.h6.codegenerator.entity.hdpos.basic.BasicBill;
import com.zxj.h6.codegenerator.entity.hdpos.basic.BasicBillLine;
import com.zxj.h6.codegenerator.entity.hdpos.basic.BasicClsBill;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangxuejun
 */
public class ExtendsClassUtils {


  /**
   * 表信息对象
   */
  private TableInfo tableInfo;

  /**
   *  是否P对象
   */
  private boolean isPEntity;

  /**
   *  继承类
   */
  private String extendsClassName = "";

  /**
   *  转化时忽略的字段
   */
  private List<String> ignoreFields = new ArrayList<>();

  public ExtendsClassUtils(TableInfo tableInfo, boolean isPEntity) {
    this.tableInfo = tableInfo;
    this.isPEntity = isPEntity;
  }

  public String getExtendsClassName() {
    return isPEntity ? getPEntityExtendsClassName() : getApiExtendsClassName();
  }

  public List<String> getIgnoreFields() {
    // 先获取继承类，再取被忽略字段
    if(StringUtils.isNullOrBlank(extendsClassName)) {
      extendsClassName = getExtendsClassName();
    }
    return ignoreFields;
  }

  private String getPEntityExtendsClassName() {
    if(containsBasicClsBillLineFields()) {
      extendsClassName = "com.hd123.hdpos4.jpa.entity.PBasicClsBillLine";
    } else if (containsBasicClsBillFields()) {
      extendsClassName = "com.hd123.hdpos4.jpa.entity.PBasicClsBill";
    } else if (containsBasicBillLineFields()) {
      extendsClassName = "com.hd123.hdpos4.jpa.entity.PBasicBillLine";
    } else if (containsBasicBillFields()) {
      extendsClassName = "com.hd123.hdpos4.jpa.entity.PBasicBill";
    } else if(containsH4csClsBillLineFields()) {
      extendsClassName = "com.hd123.h4cs.commons.jpa.entity.PClsBillLine";
    } else if (containsH4csClsBillFields()) {
      extendsClassName = "com.hd123.h4cs.commons.jpa.entity.PClsBill";
    } else if (containsH4csBaseBillLineFields()) {
      extendsClassName = "com.hd123.h4cs.commons.jpa.entity.PBaseBillLine";
    } else if (containsH4csBaseBillFields()) {
      extendsClassName = "com.hd123.h4cs.commons.jpa.entity.PBaseBill";
    }
    return extendsClassName;
  }

  private String getApiExtendsClassName() {
    if(containsBasicClsBillLineFields()) {
      extendsClassName = "com.hd123.hdpos4.biz.entity.BasicClsBillLine";
    } else if (containsBasicClsBillFields()) {
      extendsClassName = "com.hd123.hdpos4.biz.entity.BasicClsBill";
    } else if (containsBasicBillLineFields()) {
      extendsClassName = "com.hd123.hdpos4.biz.entity.BasicBillLine";
    } else if (containsBasicBillFields()) {
      extendsClassName = "com.hd123.hdpos4.biz.entity.BasicBill";
    } else if(containsH4csClsBillLineFields()) {
      extendsClassName = "com.hd123.h4cs.biz.entity.ClsBillLine";
    } else if (containsH4csClsBillFields()) {
      extendsClassName = "com.hd123.h4cs.biz.entity.ClsBill";
    } else if (containsH4csBaseBillLineFields()) {
      extendsClassName = "com.hd123.h4cs.biz.entity.BaseBillLine";
    } else if (containsH4csBaseBillFields()) {
      extendsClassName = "com.hd123.h4cs.biz.entity.BaseBill";
    }
    return extendsClassName;
  }

  public boolean containsBasicClsBillLineFields() {
    List<String> fields = getAllFields(BasicClsBillLine.class);
    List<String> columnNameList = tableInfo.getFullColumn().stream()
                                           .map(columnInfo -> columnInfo.getName().toLowerCase()).collect(
                    Collectors.toList());
    if(columnNameList.containsAll(fields)) {
      ignoreFields.addAll(fields);
      return true;
    } else {
      return false;
    }
  }

  public boolean containsBasicBillLineFields() {
    List<String> fields = getAllFields(BasicBillLine.class);
    List<String> columnNameList = tableInfo.getFullColumn().stream()
                                           .map(columnInfo -> columnInfo.getName().toLowerCase()).collect(
                    Collectors.toList());
    if(columnNameList.containsAll(fields)) {
      ignoreFields.addAll(fields);
      return true;
    } else {
      return false;
    }
  }

  public boolean containsBasicClsBillFields() {
    List<String> fields = getAllFields(BasicClsBill.class);
    List<String> columnNameList = tableInfo.getFullColumn().stream()
                                           .map(columnInfo -> columnInfo.getName().toLowerCase()).collect(
                    Collectors.toList());
    if(columnNameList.containsAll(fields)) {
      ignoreFields.addAll(fields);
      return true;
    } else {
      return false;
    }
  }

  public boolean containsBasicBillFields() {
    List<String> fields = getAllFields(BasicBill.class);
    List<String> columnNameList = tableInfo.getFullColumn().stream()
                                    .map(columnInfo -> columnInfo.getName().toLowerCase()).collect(
                    Collectors.toList());
    if(columnNameList.containsAll(fields)) {
      ignoreFields.addAll(fields);
      return true;
    } else {
      return false;
    }
  }

  public boolean containsH4csClsBillLineFields() {
    List<String> fields = getAllFields(ClsBillLine.class);
    List<String> columnNameList = tableInfo.getFullColumn().stream()
                                           .map(columnInfo -> columnInfo.getName().toLowerCase()).collect(
                    Collectors.toList());
    if(columnNameList.containsAll(fields)) {
      ignoreFields.addAll(fields);
      return true;
    } else {
      return false;
    }
  }

  public boolean containsH4csBaseBillLineFields() {
    List<String> fields = getAllFields(BaseBillLine.class);
    List<String> columnNameList = tableInfo.getFullColumn().stream()
                                           .map(columnInfo -> columnInfo.getName().toLowerCase()).collect(
                    Collectors.toList());
    if(columnNameList.containsAll(fields)) {
      ignoreFields.addAll(fields);
      return true;
    } else {
      return false;
    }
  }

  public boolean containsH4csClsBillFields() {
    List<String> fields = getAllFields(ClsBill.class);
    List<String> columnNameList = tableInfo.getFullColumn().stream()
                                           .map(columnInfo -> columnInfo.getName().toLowerCase()).collect(
                    Collectors.toList());
    if(columnNameList.containsAll(fields)) {
      ignoreFields.addAll(fields);
      return true;
    } else {
      return false;
    }
  }

  public boolean containsH4csBaseBillFields() {
    List<String> fields = getAllFields(BaseBill.class);
    List<String> columnNameList = tableInfo.getFullColumn().stream()
                                           .map(columnInfo -> columnInfo.getName().toLowerCase()).collect(
                    Collectors.toList());
    if(columnNameList.containsAll(fields)) {
      ignoreFields.addAll(fields);
      return true;
    } else {
      return false;
    }
  }

  private List<String> getAllFields(Class<?> clazz) {
    List<String> fields = new ArrayList<>();
    while (clazz != null) {
      Field[] declaredFields = clazz.getDeclaredFields();
      fields.addAll(
              Arrays.stream(declaredFields).toList().stream().filter(columnInfo -> !Arrays.asList("serialVersionUID", "customFields").contains(columnInfo.getName()))
                    .map(field -> field.getName().toLowerCase()).collect(
                      Collectors.toList()));
      // 获取父类
      clazz = clazz.getSuperclass();
    }
    return fields;
  }
}    