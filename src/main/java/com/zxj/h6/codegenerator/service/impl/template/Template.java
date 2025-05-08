 /**      
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2025，所有权利保留。      
 *      
 * 项目名：h6-codeGenerator
 * 文件名：TemplateConfig.java      
 * 模块说明：      
 * 修改历史：      
 * 2025年05月06日 - zhangxuejun - 创建。      
 */ 
package com.zxj.h6.codegenerator.service.impl.template;

 import java.util.ArrayList;
 import java.util.List;

 /**
 *  模板模型
* @author zhangxuejun        
*/     
public class Template {

   private String path;
   private String moduleId;
   private Integer moduleNo;
   private List<PackageConfig> packageConfigList = new ArrayList<>();

   public Template(String moduleId, Integer moduleNo, List<PackageConfig> packageConfigList) {
       this.moduleId = moduleId;
       this.moduleNo = moduleNo;
       this.packageConfigList = packageConfigList;
   }

   public static Template of(TemplateType templateType, List<PackageConfig> packageConfigList) {
     return new Template(templateType.getModuleId(), templateType.getModuleNo(), packageConfigList);
   }


 }