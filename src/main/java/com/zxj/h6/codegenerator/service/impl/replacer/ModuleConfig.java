/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2025，所有权利保留。
 * <p>
 * 项目名：h6-codeGenerator
 * 文件名：ModuleConfig.java
 * 模块说明：
 * 修改历史：
 * 2025年05月06日 - zhangxuejun - 创建。
 */
package com.zxj.h6.codegenerator.service.impl.replacer;

import com.zxj.h6.codegenerator.entity.SettingsStorageDTO;
import com.zxj.h6.codegenerator.service.SettingsStorageService;
import com.zxj.h6.codegenerator.service.impl.template.TemplateConstants;
import com.zxj.h6.codegenerator.service.impl.template.TemplateType;
import com.zxj.h6.codegenerator.tool.GlobalDict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangxuejun
 */
public class ModuleConfig {

  private final TemplateType templateType;
  private final String targetModuleId;
  private final Integer targetModuleNo;
  private final String targetModuleName;
  private final String targetProjectName;
  private final Map<String, String> caseVariations;
  private final List<BeanRegistrationConfig> beanRegistrations;

  public ModuleConfig(TemplateType templateType, String targetModuleId, Integer targetModuleNo,
          String targetModuleName, String targetProjectName) {
    this.templateType = templateType;
    this.targetModuleId = targetModuleId;
    this.targetModuleNo = targetModuleNo;
    this.targetModuleName = targetModuleName;
    this.targetProjectName = targetProjectName;
    this.caseVariations = generateCaseVariations(templateType.getModuleId(), targetModuleId);
    this.beanRegistrations = new ArrayList<>();
  }

  public void addBeanRegistration(BeanRegistrationConfig config) {
    this.beanRegistrations.add(config);
  }

  private Map<String, String> generateCaseVariations(String original, String replacement) {
    Map<String, String> variations = new HashMap<>();

    // 驼峰命名 (首字母小写)
    variations.put(original, replacement);

    // 帕斯卡命名 (首字母大写)
    variations.put(
            original.substring(0, 1).toUpperCase() + original.substring(1),
            replacement.substring(0, 1).toUpperCase() + replacement.substring(1)
    );

    // 全小写
    variations.put(original.toLowerCase(), replacement.toLowerCase());

    // 全大写
    variations.put(original.toUpperCase(), replacement.toUpperCase());

    // 替换模块编号
    variations.put(templateType.getModuleNo().toString(), targetModuleNo.toString());

    // 替换模块名称
    variations.put(templateType.getName(), targetModuleName);

    // 替换包名中的模块名
    variations.put("." + PathConstants.BASE_TEMPLATE_MODULE + ".", "." + targetProjectName + ".");

    // 包路径替换成小写
    variations.put(templateType.getPackageSufix().replace("/", "."), targetModuleId.toLowerCase());

    // 替换JS文件中的模块名
    variations.put("'Template.", "'" + targetProjectName.substring(0, 1).toUpperCase() + targetProjectName.substring(1) + ".");

    // 替换XML文件中的模块名
    variations.put("-template-", "-" + targetProjectName + "-");

    // 替换文件的创建人
    SettingsStorageDTO settingsStorageDto = SettingsStorageService.getSettingsStorage();
    if (settingsStorageDto != null) {
      variations.put(GlobalDict.AUTHOR, settingsStorageDto.getAuthor());
    }

    return variations;
  }

  public Map<String, String> getCaseVariations() {
    return caseVariations;
  }

  public List<BeanRegistrationConfig> getBeanRegistrations() {
    return beanRegistrations;
  }
}