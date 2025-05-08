package com.zxj.h6.codegenerator.tool;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.search.GlobalSearchScope;
import com.zxj.h6.codegenerator.entity.ClassField;
import java.util.*;

public class ClassBasedGenerator {
    private final Project project;
    private final PsiClass templateClass;
    private final String sourcePattern;
    private final String targetPattern;

    public ClassBasedGenerator(Project project, String templateClassName, String sourcePattern, String targetPattern) {
        this.project = project;
        this.sourcePattern = sourcePattern;
        this.targetPattern = targetPattern;
        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
        this.templateClass = javaPsiFacade.findClass(templateClassName, GlobalSearchScope.allScope(project));
        if (this.templateClass == null) {
            throw new IllegalArgumentException("Template class not found: " + templateClassName);
        }
    }

    private String replacePattern(String input) {
        if (sourcePattern == null || targetPattern == null) {
            return input;
        }

        // 处理驼峰式命名中的模式替换
        String result = input;
        
        // 替换完全匹配的情况
        result = result.replace(sourcePattern, targetPattern);
        
        // 替换首字母小写的情况
        String lowerSource = sourcePattern.substring(0, 1).toLowerCase() + sourcePattern.substring(1);
        String lowerTarget = targetPattern.substring(0, 1).toLowerCase() + targetPattern.substring(1);
        result = result.replace(lowerSource, lowerTarget);
        
        // 替换全大写的情况
        String upperSource = sourcePattern.toUpperCase();
        String upperTarget = targetPattern.toUpperCase();
        result = result.replace(upperSource, upperTarget);
        
        // 替换全小写的情况
        String allLowerSource = sourcePattern.toLowerCase();
        String allLowerTarget = targetPattern.toLowerCase();
        result = result.replace(allLowerSource, allLowerTarget);

        return result;
    }

    public List<ClassField> getFields() {
        List<ClassField> fields = new ArrayList<>();
        PsiField[] psiFields = templateClass.getAllFields();
        
        for (PsiField field : psiFields) {
            if (!field.hasModifierProperty(PsiModifier.STATIC)) {
                ClassField classField = new ClassField();
                classField.setName(replacePattern(field.getName()));
                classField.setType(replacePattern(field.getType().getPresentableText()));
                
                // Get field comment if exists
                PsiDocComment docComment = field.getDocComment();
                if (docComment != null) {
                    classField.setComment(replacePattern(docComment.getText()));
                }
                
                fields.add(classField);
            }
        }
        
        return fields;
    }

    public String generateCode(String packageName, String className) {
        StringBuilder code = new StringBuilder();
        
        // Generate package declaration
        code.append("package ").append(packageName).append(";\n\n");
        
        // Generate imports
        Set<String> imports = new HashSet<>();
        for (ClassField field : getFields()) {
            String type = field.getType();
            if (type.contains(".")) {
                String importStatement = "import " + replacePattern(type) + ";";
                imports.add(importStatement);
            }
        }

        // 添加模板类的所有imports
        PsiFile file = templateClass.getContainingFile();
        if (file instanceof PsiJavaFile) {
            PsiImportList importList = ((PsiJavaFile) file).getImportList();
            if (importList != null) {
                for (PsiImportStatement importStatement : importList.getImportStatements()) {
                    String importText = replacePattern(importStatement.getText());
                    imports.add(importText);
                }
            }
        }

        for (String importStatement : imports) {
            code.append(importStatement).append("\n");
        }
        code.append("\n");
        
        // 获取类的注释
        PsiDocComment classComment = templateClass.getDocComment();
        if (classComment != null) {
            code.append(replacePattern(classComment.getText())).append("\n");
        }

        // Generate class with modifiers
        String modifiers = templateClass.getModifierList() != null ? templateClass.getModifierList().getText() + " " : "";
        code.append(modifiers).append("class ").append(className);
        
        // 添加extends
        if (templateClass.getExtendsList() != null && templateClass.getExtendsList().getReferencedTypes().length > 0) {
            PsiClassType[] extendsTypes = templateClass.getExtendsList().getReferencedTypes();
            if (extendsTypes.length > 0) {
                code.append(" extends ").append(replacePattern(extendsTypes[0].getPresentableText()));
            }
        }
        
        // 添加implements
        if (templateClass.getImplementsList() != null && templateClass.getImplementsList().getReferencedTypes().length > 0) {
            PsiClassType[] implementsTypes = templateClass.getImplementsList().getReferencedTypes();
            if (implementsTypes.length > 0) {
                code.append(" implements ");
                for (int i = 0; i < implementsTypes.length; i++) {
                    if (i > 0) {
                        code.append(", ");
                    }
                    code.append(replacePattern(implementsTypes[i].getPresentableText()));
                }
            }
        }
        
        code.append(" {\n\n");
        
        // Generate fields
        for (ClassField field : getFields()) {
            if (field.getComment() != null) {
                code.append("    ").append(field.getComment()).append("\n");
            }
            code.append("    private ").append(field.getType())
                .append(" ").append(field.getName()).append(";\n\n");
        }
        
        // Generate methods from template class
        for (PsiMethod method : templateClass.getMethods()) {
            if (!method.isConstructor()) {
                // 添加方法注释
                if (method.getDocComment() != null) {
                    code.append("    ").append(replacePattern(method.getDocComment().getText())).append("\n");
                }
                
                // 添加方法体
                String methodText = method.getText();
                code.append("    ").append(replacePattern(methodText)).append("\n\n");
            } else {
                // 处理构造函数，需要改名为新类名
                String constructorText = method.getText();
                constructorText = constructorText.replace(templateClass.getName(), className);
                code.append("    ").append(replacePattern(constructorText)).append("\n\n");
            }
        }
        
        code.append("}\n");
        return code.toString();
    }
} 