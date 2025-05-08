/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2025，所有权利保留。
 * <p>
 * 项目名：h6-codeGenerator
 * 文件名：FileProcessor.java
 * 模块说明：
 * 修改历史：
 * 2025年05月06日 - zhangxuejun - 创建。
 */
package com.zxj.h6.codegenerator.service.impl.replacer;

import com.intellij.openapi.diagnostic.Logger;
import com.zxj.h6.codegenerator.service.impl.NewCodeGenerateServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author zhangxuejun
 */
public class FileProcessor {

  private static final Logger logger = Logger.getInstance(FileProcessor.class);


  private final ModuleConfig config;
  private final Path sourceDir;
  private final Path targetDir;
  private final List<DateTimeFormatter> dateFormatters;
  private final LocalDate currentDate;

  private FileProcessor(ModuleConfig config, String sourceDir, String targetDir) {
    this.config = config;
    this.sourceDir = Paths.get(sourceDir);
    this.targetDir = Paths.get(targetDir);
    this.dateFormatters = Arrays.asList(DateTimeFormatter.ofPattern("yyyy年MM月dd日"), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    this.currentDate = LocalDate.now();
  }

  public static FileProcessor of(ModuleConfig config, String sourceDir, String targetDir) {
    return new FileProcessor(config, sourceDir, targetDir);
  }

  public void processFiles() throws IOException {
    // 确保目标目录存在
    Files.createDirectories(targetDir);

    // 检查源目录是否存在
    if (!Files.exists(this.sourceDir)) {
      logger.warn("Source directory does not exist: " + sourceDir);
      return;
    }
    if (!Files.isDirectory(this.sourceDir)) {
      logger.warn("Source path is not a directory: " + sourceDir);
      return;
    }

    // 遍历源目录中的所有文件
    Files.walk(sourceDir)
         .filter(Files::isRegularFile)
         .forEach(this::processFile);
  }

  private void processFile(Path sourceFile) {
    try {
      // 读取文件内容
      String content = new String(Files.readAllBytes(sourceFile));

      // 替换内容
      String newContent = replaceContent(content);

      // 获取相对路径并替换文件名
      Path relativePath = sourceDir.relativize(sourceFile);
      String fileName = relativePath.getFileName().toString();
      String newFileName = replaceFileName(fileName);

      // 创建新的相对路径
      Path newRelativePath;
      Path parent = relativePath.getParent();
      if (parent != null) {
        // 如果有父目录，使用父目录路径
        newRelativePath = parent.resolve(newFileName);
      } else {
        // 如果没有父目录（文件在根目录），直接使用新文件名
        newRelativePath = Paths.get(newFileName);
      }

      // 创建目标文件路径
      Path targetFile = targetDir.resolve(newRelativePath);

      // 确保目标目录存在
      Files.createDirectories(targetFile.getParent());

      // 写入新内容
      Files.write(targetFile, newContent.getBytes());

      System.out.println("Processed: " + sourceFile + " -> " + targetFile);
    } catch (IOException e) {
      System.err.println("Error processing file: " + sourceFile);
      e.printStackTrace();
    }
  }

  /**
   * 替换文件名
   */
  private String replaceFileName(String fileName) {
    String result = fileName;
    // 获取文件名（不含扩展名）和扩展名
    int lastDotIndex = fileName.lastIndexOf('.');
    String nameWithoutExt = lastDotIndex > 0 ? fileName.substring(0, lastDotIndex) : fileName;
    String extension = lastDotIndex > 0 ? fileName.substring(lastDotIndex) : "";

    // 替换文件名中的模块ID
    for (Map.Entry<String, String> entry : config.getCaseVariations().entrySet()) {
      String pattern = Pattern.quote(entry.getKey());
      nameWithoutExt = nameWithoutExt.replaceAll(pattern, entry.getValue());
    }

    // 重新组合文件名和扩展名
    return nameWithoutExt + extension;
  }

  /**
   * 替换规则内的文件内容
   */
  private String replaceContent(String content) {
    String result = content;
    for (Map.Entry<String, String> entry : config.getCaseVariations().entrySet()) {
      String pattern = Pattern.quote(entry.getKey());
      result = result.replaceAll(pattern, entry.getValue());
    }

    // 替换日期
    result = replaceDates(result);

    return result;
  }

  /**
   * 替换日期格式
   */
  private String replaceDates(String content) {
    int index = 0;
    for(DateTimeFormatter dateFormatter : dateFormatters) {
      String currentDateStr = currentDate.format(dateFormatter);
      String regex = index == 0 ? "\\d{4}年\\d{2}月\\d{2}日" : "\\d{4}/\\d{1,2}/\\d{1,2}";
      Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
      content = pattern.matcher(content).replaceAll(currentDateStr);
      index++;
    }
    return content;
  }
}    