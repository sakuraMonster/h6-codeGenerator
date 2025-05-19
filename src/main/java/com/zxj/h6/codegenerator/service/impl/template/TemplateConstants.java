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

  public static final Map<String, String> constantsJSContexts = new HashMap<String, String>();


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

  public static final String PLACE_HOLDER_0 = "{0}";

  public static final String PLACE_HOLDER_1 = "{1}";

  public static final String PLACE_HOLDER_2 = "{2}";

  public static final String PLACE_HOLDER_3 = "{3}";

  public static final String PLACE_HOLDER_4 = "{4}";

  static {
    String serverXml = BASE_SERVER_XML + "template-server.xml";
    String serverXmlContext = "\n  <bean lazy-init=\"true\" id=\"hdpos4-template-api.{0}Service\"\n"
            + "        class=\"com.hd123.hdpos4.template.impl.{1}.{2}ServiceImpl\"/>";

    String logXml = BASE_LOG_XML + "hdpos4-log.xml";
    String logXmlContext = "\n        <entry key=\"{0}\" value=\"{0}Log\"/>";

    String sysXml = BASE_SYS_XML + "sys-core.xml";
    String sysDqueryXmlContext = "\n        <entry key=\"{0}\"\n"
            + "               value=\"com.hd123.hdpos4.template.service.{1}.{2}s\"/>";

    String msgXml = BASE_MSG_XML + "hdpos4-msg.xml";
    String msgXmlClientContext = "\n        <entry key=\"{0}\"\n"
            + "               value=\"com.hd123.hdpos4.template.service.{1}.{2}ClientMsgs\" />";
    String msgXmlServerContext = "\n        <entry key=\"{0}\"\n"
            + "               value=\"com.hd123.hdpos4.template.service.{1}.{2}ServerMsgs\" />";
    String msgXmlComboContext = "\n        <entry key=\"{0}\"\n"
            + "               value=\"com.hd123.hdpos4.template.service.{1}.{2}ComboMsgs\" />";

    String constantsJS = "        {0}: {\n" +
            "            id: {1},\n" +
            "            name: \"{2}\",\n" +
            "            servicePath: 'hdpos4/template/{3}',\n" +
            "            viewType: '{4}search'\n" +
            "        }";

    for (TemplateType templateType : TemplateType.values()) {
      String firstUpperModuleId = templateType.getModuleId().substring(0, 1).toUpperCase() + templateType.getModuleId().substring(1);
      String packageSufix = templateType.getPackageSufix().replace("/", ".");
      serverXmls.put(templateType.getModuleId(), serverXml);
      serverXmlContexts.put(templateType.getModuleId(),
              serverXmlContext.replace(PLACE_HOLDER_0, templateType.getModuleId()).replace(PLACE_HOLDER_1, packageSufix)
                      .replace(PLACE_HOLDER_2, firstUpperModuleId));

      logXmls.put(templateType.getModuleId(), logXml);
      logXmlContexts.put(templateType.getModuleId(),logXmlContext.replace(PLACE_HOLDER_0, templateType.getModuleId()));

      sysXmls.put(templateType.getModuleId(), sysXml);
      sysDqueryXmlContexts.put(templateType.getModuleId(),
              sysDqueryXmlContext.replace(PLACE_HOLDER_0, templateType.getModuleId())
                      .replace(PLACE_HOLDER_1, packageSufix)
                      .replace(PLACE_HOLDER_2, firstUpperModuleId));

      msgXmls.put(templateType.getModuleId(), msgXml);
      msgXmlClientContexts.put(templateType.getModuleId(),
              msgXmlClientContext.replace(PLACE_HOLDER_0, templateType.getModuleId())
                      .replace(PLACE_HOLDER_1, packageSufix)
                      .replace(PLACE_HOLDER_2, firstUpperModuleId));
      msgXmlServerContexts.put(templateType.getModuleId(),
              msgXmlServerContext.replace(PLACE_HOLDER_0, templateType.getModuleId())
                      .replace(PLACE_HOLDER_1, packageSufix)
                      .replace(PLACE_HOLDER_2, firstUpperModuleId));
      msgXmlComboContexts.put(templateType.getModuleId(),
              msgXmlComboContext.replace(PLACE_HOLDER_0, templateType.getModuleId())
                      .replace(PLACE_HOLDER_1, packageSufix)
                      .replace(PLACE_HOLDER_2, firstUpperModuleId));

      constantsJSContexts.put(templateType.getModuleId(),
              constantsJS.replace(PLACE_HOLDER_0, templateType.getModuleId().toUpperCase())
                      .replace(PLACE_HOLDER_1, templateType.getModuleNo().toString())
                      .replace(PLACE_HOLDER_2, templateType.getModuleId())
                      .replace(PLACE_HOLDER_3, templateType.getModuleId().toLowerCase())
                      .replace(PLACE_HOLDER_4, templateType.getModuleId().toLowerCase()));

    }

  }

}    