package com.zxj.h6.codegenerator.sqlparser;

import com.zxj.h6.codegenerator.tool.StringUtils;
import org.apache.commons.collections.CollectionUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangxuejun
 */
public class SqlParser {

  /** 注释符号 */
  protected String commentSymbol;
  /** 表名 */
  public String tableName;
  /** 表的注释名 */
  public String tableComment;

  /** 表信息对应关系 */
  public Map<String, String> tableNameMap = new HashMap<>();
  /** 表备注信息对应关系 */
  public Map<String, String> tableCommentMap = new HashMap<>();
  /** 表->字段信息 */
  public Map<String, List<ColumnCategory>> columnCategoriesMap = new HashMap<>();
  /** 来源sql */
  protected StringBuffer sourceSqlBuffer;
  /** 字段结构 */
  public List<ColumnCategory> columnCategories;
  /** 是否为升级脚本true-是，false-否(安装) */
  protected boolean upgrade = false;

  public SqlParser(StringBuffer sourceSqlBuffer){
      initCommentSymbol();
      parseComment(sourceSqlBuffer);
  }

  /**
   * 获取带注释的sql
   *
   * @return
   */
  protected List<String> getCommentSqls() {
    List<String> result = new ArrayList<>();
    if (!StringUtils.isNullOrBlank(tableComment)) {
      String commentTableTemplate = "COMMENT ON TABLE {0} IS {1}";
      result.add(MessageFormat.format(commentTableTemplate, tableName, "'" + tableComment + "'"));
    }

    String commentTemplate = "COMMENT ON COLUMN {0} IS {1}";
    for (ColumnCategory columnCategory : columnCategories) {
      if (StringUtils.isNullOrBlank(columnCategory.getComment())) {
        continue;
      }
      result.add(MessageFormat.format(commentTemplate, tableName + "." + columnCategory.getColumn(),
          "'" + columnCategory.getComment() + "'"));
    }
    return result;
  }

  /**
   * 获取修改的建表语句
   *
   * @return
   */
  protected List<String> getDecorateCreateSqls() {
    if (StringUtils.isNullOrBlank(tableName) || (StringUtils.isNullOrBlank(
        tableComment) && CollectionUtils.isEmpty(columnCategories))) {
      return new ArrayList<>();
    }
    if (upgrade && isExecuteSql(sourceSqlBuffer.toString())) {
      return new ArrayList<>();
    }
    List<String> result = new ArrayList<>();
    result.add(sourceSqlBuffer.toString());
    result.addAll(getCommentSqls());
    return result;
  }

  protected void initCommentSymbol() {
    this.commentSymbol = "--";
  }

  /**
   * 初始化建表语句的基本信息如下： 初始化tableName（表名）、tableComment（表的注释名） 初始化ColumnCategory
   *
   * @param sqlBuffer
   */
  protected void parseComment(StringBuffer sqlBuffer) {
    if (StringUtils.isNullOrBlank(sqlBuffer.toString())) {
      return;
    }
    String[] sqls = sqlBuffer.toString().split(";");
    for(String sql : sqls) {
      boolean result = false;
      if (upgrade) {
        result = parseUpdComment(sql);
      } else {
        result = parseSetUpComment(sql);
      }
      if (!result) {
        continue;
      }

      List<ColumnCategory> targetColumnCategories = new ArrayList<>();
      // 获取建表语句的字段部分sql数据
      String createColumnStr = getCreateColumnStr(sql);

      // 获取重建后的建表语句的字段部分sql数据（建表语句的“空格,\t,\n”混用，无法直接解析，需要重组）
      List<String> reBuildColumnSqls = getReBuildColumnSqls(createColumnStr);
      for (String rowSql : reBuildColumnSqls) {
        // 获取多个字段的解析结果集
        List<ColumnCategory> rowSqlCategories = getRowSqlCategories(rowSql);
        targetColumnCategories.addAll(rowSqlCategories);
      }
      columnCategoriesMap.put(tableName.toLowerCase(), targetColumnCategories);
    }

  }

  private boolean parseUpdComment(String sql) {

    String regex = "\\bCREATE\\s+(?:GLOBAL\\s+TEMPORARY\\s+)?TABLE\\s+(\\w+)\\s*(?:\\s*\\(?\\s*" + commentSymbol + "\\s*([\\p{L} \\d]+)?)?";
    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(sql);
    if (!matcher.find()) {
      return false;
    }
    tableName = matcher.group(1);
    tableComment = matcher.group(2);

    regex = "\\bCREATE\\s+TABLE\\s+IF\\s+NOT\\s+EXISTS\\s+(\\w+)\\s*(?:\\s*\\(\\s*" + commentSymbol + "\\s*([\\p{L} \\d]+)?)?";
    pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    matcher = pattern.matcher(sql);
    if (matcher.find()) {
      tableName = matcher.group(1);
      tableComment = matcher.group(2);
    }
    return true;
  }

  private boolean parseSetUpComment(String sql) {
    String regex = "CREATE\\s+OR\\s+REPLACE\\s+(PROCEDURE|FUNCTION|PACKAGE|VIEW)\\b";
    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(sql);
    if (matcher.find()) {
      return false;
    }

    regex = "CREATE\\s+(?:GLOBAL\\s+TEMPORARY\\s+)?TABLE\\s+";
    pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    matcher = pattern.matcher(sql);
    if (!matcher.find()) {
      return false;
    }

    regex = "CREATE\\s+(?:GLOBAL\\s+TEMPORARY\\s+)?TABLE\\s+(\\w+)\\s*(?:\\s*\\(?\\s*" + commentSymbol + "\\s*([\\p{L} \\d]+)?)?";
    pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    matcher = pattern.matcher(sql);
    if (!matcher.find()) {
      return false;
    }
    tableName = matcher.group(1);
    tableComment = matcher.group(2);
    tableNameMap.put(tableName.toLowerCase(), tableName);

    regex = "CREATE\\s+TABLE\\s+IF\\s+NOT\\s+EXISTS\\s+(\\w+)\\s*(?:\\s*\\(\\s*" + commentSymbol + "\\s*([\\p{L} \\d]+)?)?";
    pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    matcher = pattern.matcher(sql);
    if (matcher.find()) {
      tableName = matcher.group(1);
      tableComment = matcher.group(2);
      tableCommentMap.put(tableName.toLowerCase(), tableComment);
    }
    return true;
  }

  protected boolean isExecuteSql(String sql) {
    if (StringUtils.isNullOrBlank(sql)) {
      return false;
    }
    String regex = "EXECUTE\\s+IMMEDIATE";
    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(sql);
    if (matcher.find()) {
      return true;
    }
    return false;
  }

  protected String getCreateColumnStr(String sourceSql) {
    String step = null;
    String regex = "CREATE\\s+(?:GLOBAL\\s+TEMPORARY\\s+)?TABLE\\s+";
    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    Matcher matcher = null;
    String[] rowSqlArr = sourceSql.split("\n");
    StringBuilder newColumnBuilder = new StringBuilder();
    int quoteCount = 0;
    for (int i = 0; i < rowSqlArr.length; i++) {
      String rowSql = rowSqlArr[i];
      matcher = pattern.matcher(rowSql);
      if (matcher.find()) {
        step = "CREATE_TABLE";
      }
      if ("CREATE_TABLE".equals(step)) {
        if (rowSql.contains("(")) {
          step = "FIND_COLUMN";
          quoteCount = 1;
        }
        continue;
      }
      if ("FIND_COLUMN".equals(step)) {
        int charIndex = 0;
        int commentSymbolIndex = rowSql.indexOf(commentSymbol);
        for (char s : rowSql.toCharArray()) { // 括号入栈，出栈以寻找语句create table XXX()括号内的sql
          if (commentSymbolIndex == -1 || charIndex < commentSymbolIndex) { // 需要排除掉注释里的括号
            if (s == '(') {
              quoteCount++;
            } else if (s == ')') {
              quoteCount--;
            }
            if (quoteCount == 0) {
              break;
            }
          }
          charIndex++;
        }
        if (quoteCount == 0) {
          newColumnBuilder.append(rowSql, 0, charIndex).append("\n");
          break;
        } else {
          newColumnBuilder.append(rowSql).append("\n");
        }
      }
    }
    return newColumnBuilder.toString();
  }

  protected List<String> getReBuildColumnSqls(String sourceSql) {
    String[] splitSqls = sourceSql.split("\n");
    if (splitSqls.length == 0) {
      return new ArrayList<>();
    }
    int quoteCount = 0;
    List<String> rows = new ArrayList<>();
    StringBuilder sb = new StringBuilder();
    for (String splitSql : splitSqls) {
      if (StringUtils.isNullOrBlank(splitSql)) {
        continue;
      }
      sb.append(splitSql).append(" ");
      for (char s : splitSql.toCharArray()) {
        if (s == '(') {
          quoteCount++;
        } else if (s == ')') {
          quoteCount--;
        }
      }
      if (quoteCount == 0) {
        if (splitSql.contains(commentSymbol) || (!splitSql.contains(
            commentSymbol) && splitSql.contains(","))) {
          rows.add(sb.toString());
          sb = new StringBuilder();
        }
      }
    }
    if (sb.length() > 0) {
      rows.add(sb.toString());
    }

    return rows;
  }

  protected List<ColumnCategory> getRowSqlCategories(String sourceSql) {
    String[] split = sourceSql.split(commentSymbol);
    String regex = ",(?![^\\(]*\\))";
    String[] splitSqls = split[0].split(regex);
    List<ColumnCategory> targetSqlCategory = new ArrayList<>();
    String constraintRegex = "^(PRIMARY\\s+KEY|UNIQUE|FOREIGN\\s+KEY|INDEX|FULLTEXT\\s+)\\b.*";
    Matcher matcher = null;
    Pattern pattern = Pattern.compile(constraintRegex, Pattern.CASE_INSENSITIVE);
    for (String splitSql : splitSqls) {
      matcher = pattern.matcher(splitSql.trim());
      if (matcher.find()) {
        // SqlCategory sqlCategory = new SqlCategory();
        // sqlCategory.setType("CONSTRAINT");
        // sqlCategory.setConstant(splitSql);
        // targetSqlCategory.add(sqlCategory);
        continue;
      }
      // 正则表达式：匹配空格、制表符或换行符
      String[] splitCategory = Arrays.stream(splitSql.split("\\s+(?![^\\(]*\\))"))
          .filter(s -> !s.isEmpty()).toArray(String[]::new);
      if (splitCategory.length >= 2) {
        ColumnCategory sqlCategory = new ColumnCategory();
        sqlCategory.setColumn(splitCategory[0]);
        sqlCategory.setType(splitCategory[1]);
        targetSqlCategory.add(sqlCategory);
        if (splitCategory.length >= 3) {
          StringBuilder constantBuilder = new StringBuilder();
          for (int i = 0; i < splitCategory.length; i++) {
            if (i >= 2) {
              if ("UNIQUE".equalsIgnoreCase(splitCategory[i])) {
                continue;
              }
              if ("AUTO_INCREMENT".equalsIgnoreCase(splitCategory[i])) {
                continue;
              }
              if (i + 1 < splitCategory.length) {
                if ("PRIMARY".equalsIgnoreCase(splitCategory[i]) && "KEY".equalsIgnoreCase(
                    splitCategory[i + 1])) {
                  continue;
                }
              }
              if (i - 1 >= 0) {
                if ("PRIMARY".equalsIgnoreCase(splitCategory[i - 1]) && "KEY".equalsIgnoreCase(
                    splitCategory[i])) {
                  continue;
                }
              }
              constantBuilder.append(splitCategory[i]).append(" ");
            }
          }
          sqlCategory.setConstant(constantBuilder.toString());
        }
      }
    }
    if (!targetSqlCategory.isEmpty() && split.length > 1) {
      targetSqlCategory.get(targetSqlCategory.size() - 1).setComment(split[1].trim());
    }
    return targetSqlCategory;
  }

}