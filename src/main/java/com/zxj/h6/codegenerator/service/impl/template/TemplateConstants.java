/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2025，所有权利保留。
 * <p>
 * 项目名：h6-codeGenerator
 * 文件名：TemplatePackageSufixs.java
 * 模块说明：
 * 修改历史：
 * 2025年05月07日 - zhangxuejun - 创建。
 */
package com.zxj.h6.codegenerator.service.impl.template;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangxuejun
 */
public class TemplateConstants {

  /** 包名后缀 */
  public static final Map<String, String> packageSufixs = new HashMap<String, String>();

  public static final Map<String, String> serverXmls = new HashMap<String, String>();

  public static final Map<String, String> serverXmlContexts = new HashMap<String, String>();

  public static final Map<String, String> logXmls = new HashMap<String, String>();

  public static final Map<String, String> logXmlContexts = new HashMap<String, String>();

  public static final Map<String, String> sysXmls = new HashMap<String, String>();

  public static final Map<String, String> sysDqueryXmlContexts = new HashMap<String, String>();

  public static final Map<String, String> msgXmls = new HashMap<String, String>();

  public static final Map<String, String> msgXmlClientContexts = new HashMap<String, String>();

  public static final Map<String, String> msgXmlServerContexts = new HashMap<String, String>();

  public static final Map<String, String> msgXmlComboContexts = new HashMap<String, String>();


  public static final String BASE_SYS_XML = "src/main/resources/META-INF/hdpos4-sys-core/";

  public static final String BASE_LOG_XML = BASE_SYS_XML;

  public static final String BASE_PACKAGE_SUFIX = "bill/";

  public static final String BASE_SERVER_XML = "src/main/resources/META-INF/template-core/";

  public static final String BASE_MSG_XML = BASE_SYS_XML;

  public static final String CLIENT_MESSSAGE_BEANID = "hdpos4-commons-core.clientMessages";

  public static final String SYS_DQUERY_BEANID = "hdpos4-sys-core.dQueryUtil";

  public static final String LOG_BEANID = "hdpos4-core.billLogTables";

  public static final String SERVER_MESSSAGE_BEANID = "hdpos4-commons-core.serverMessages";

  public static final String COMBO_MESSSAGE_BEANID = "hdpos4-commons-core.comboMessages";

  static {
    packageSufixs.put(TemplateType.BillTemplateA.getModuleId(), BASE_PACKAGE_SUFIX + "t1");

    serverXmls.put(TemplateType.BillTemplateA.getModuleId(), BASE_SERVER_XML + "template-server.xml");
    serverXmlContexts.put(TemplateType.BillTemplateA.getModuleId(), "\n  <bean lazy-init=\"true\" id=\"hdpos4-template-api.billTemplateAService\"\n"
            + "        class=\"com.hd123.hdpos4.template.impl.bill.t1.BillTemplateAServiceImpl\"/>");

    logXmls.put(TemplateType.BillTemplateA.getModuleId(), BASE_LOG_XML + "hdpos4-log.xml");
    logXmlContexts.put(TemplateType.BillTemplateA.getModuleId(), "\n        <entry key=\"billTemplateA\" value=\"billTemplateALog\"/>");

    sysXmls.put(TemplateType.BillTemplateA.getModuleId(), BASE_SYS_XML + "sys-core.xml");
    sysDqueryXmlContexts.put(TemplateType.BillTemplateA.getModuleId(), "\n        <entry key=\"billTemplateA\"\n"
            + "               value=\"com.hd123.hdpos4.template.service.bill.t1.BillTemplateAs\"/>");

    msgXmls.put(TemplateType.BillTemplateA.getModuleId(), BASE_MSG_XML + "hdpos4-msg.xml");
    msgXmlClientContexts.put(TemplateType.BillTemplateA.getModuleId(), "\n        <entry key=\"billTemplateA\"\n"
            + "               value=\"com.hd123.hdpos4.template.service.bill.t1.BillTemplateAClientMsgs\" />");
    msgXmlServerContexts.put(TemplateType.BillTemplateA.getModuleId(), "\n        <entry key=\"billTemplateA\"\n"
            + "               value=\"com.hd123.hdpos4.template.service.bill.t1.BillTemplateAServerMsgs\" />");
    msgXmlComboContexts.put(TemplateType.BillTemplateA.getModuleId(), "\n        <entry key=\"billTemplateA\"\n"
            + "               value=\"com.hd123.hdpos4.template.service.bill.t1.BillTemplateAComboMsgs\" />");



  }

}    