package com.zxj.h6.codegenerator.ui;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ExceptionUtil;
import com.zxj.h6.codegenerator.service.NewCodeGenerateService;
import com.zxj.h6.codegenerator.tool.GlobalDict;
import com.zxj.h6.codegenerator.tool.ModuleUtils;
import com.zxj.h6.codegenerator.service.impl.template.ProjectModuleType;
import com.zxj.h6.codegenerator.tool.ProjectUtils;
import com.zxj.h6.codegenerator.service.impl.template.TemplateType;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class ModuleSelectPath extends DialogWrapper {
  private JPanel contentPane;

  /**
   * 模型下拉框
   */
  private JComboBox<String> moduleComboBox;

  private JComboBox<String> templateComboBox;

  /**
   *  模块Id
   */
  private JTextField moduleIdField;

  /**
   *  模块号
   */
  private JTextField moduleNoField;

  /**
   *  模块名
   */
  private JTextField moduleNameField;


  /**
   * 项目对象
   */
  private Project project;

  /**
   * 代码生成服务
   */
  private NewCodeGenerateService codeGenerateService;

  /**
   * 当前项目中的module
   */
  private List<Module> moduleList;


  public ModuleSelectPath(Project project) {
    super(project);
    this.project = project;
    this.codeGenerateService = NewCodeGenerateService.getInstance(project);
    // 初始化module，存在资源路径的排前面
    this.moduleList = new LinkedList<>();
    for (Module module : ModuleManager.getInstance(project).getModules()) {
      // 存在源代码文件夹放前面，否则放后面
      if (ModuleUtils.existsSourcePath(module)) {
        this.moduleList.add(0, module);
      } else {
        this.moduleList.add(module);
      }
    }
    this.initPanel();
    this.refreshData();
    this.initEvent();
    init();
    setTitle("H6 Code Generator Info");
    //初始化路径
    // refreshPath();
  }

  /**
   * 初始化方法
   */
  private void initPanel() {
    //初始化Module选择
//    for (Module module : this.moduleList) {
//      moduleComboBox.addItem(module.getName());
//    }

    // 模块工程
    ProjectModuleType[] projectModuleTypes = ProjectModuleType.values();
    for(ProjectModuleType projectModuleType : projectModuleTypes) {
      moduleComboBox.addItem(projectModuleType.encode());
    }

    // 单据模板A
    templateComboBox.addItem(TemplateType.BillTemplateA.encode());
    templateComboBox.addItem(TemplateType.BillTemplateB.encode());
    templateComboBox.addItem(TemplateType.BillTemplateC.encode());
  }

  private void refreshData() {

    // todo
  }

  private void initEvent() {
  }

  /**
   * 获取基本路径
   *
   * @return 基本路径
   */
  private String getBasePath() {
    Module module = getSelectModule();
    VirtualFile baseVirtualFile = ProjectUtils.getBaseDir(project);
    if (baseVirtualFile == null) {
      Messages.showWarningDialog("无法获取到项目基本路径！", GlobalDict.TITLE_INFO);
      return "";
    }
    String baseDir = baseVirtualFile.getPath();
    if (module != null) {
      VirtualFile virtualFile = ModuleUtils.getSourcePath(module);
      if (virtualFile != null) {
        baseDir = virtualFile.getPath();
      }
    }
    return baseDir;
  }

  /**
   * 获取选中的Module
   *
   * @return 选中的Module
   */
  private Module getSelectModule() {
    String name = (String) moduleComboBox.getSelectedItem();
    if (StringUtils.isEmpty(name)) {
      return null;
    }
    return ModuleManager.getInstance(project).findModuleByName(name);
  }

  @Override
  protected void doOKAction() {
    onOK();
    super.doOKAction();
  }

  private void onOK() {
    // todo
    String moduleId = moduleIdField.getText();
    if(StringUtils.isEmpty(moduleId)) {
      Messages.showErrorDialog("Can't Write Your Module's ModuleId", GlobalDict.TITLE_INFO);
      return;
    }
    String moduleNo = moduleNoField.getText();
    if(StringUtils.isEmpty(moduleNo)) {
      Messages.showErrorDialog("Can't Write Your Module's ModuleNo", GlobalDict.TITLE_INFO);
      return;
    }

    String moduleName = moduleNameField.getText();
    if(StringUtils.isEmpty(moduleName)) {
      Messages.showErrorDialog("Can't Write Your Module's ModuleName", GlobalDict.TITLE_INFO);
      return;
    }

    String projectModule = (String) moduleComboBox.getSelectedItem();
    ProjectModuleType projectModuleType = ProjectModuleType.decode(projectModule);

    String template = (String) templateComboBox.getSelectedItem();
    TemplateType templateType = TemplateType.decode(template);

    codeGenerateService.generate(templateType, projectModuleType, moduleId,
            com.zxj.h6.codegenerator.tool.StringUtils.toInteger(moduleNo), moduleName);

  }

  private void onCancel() {
    // add your code here if necessary
    dispose();
  }

  public static void main(String[] args) {
    ModuleSelectPath dialog = new ModuleSelectPath(null);
    dialog.pack();
    dialog.show();
    System.exit(0);
  }

  @Override protected @Nullable JComponent createCenterPanel() {
    return this.contentPane;
  }
}
