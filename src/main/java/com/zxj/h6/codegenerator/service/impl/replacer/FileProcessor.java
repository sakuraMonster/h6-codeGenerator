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

    // 处理Bean注册
    processBeanRegistrations();
  }

  public void processBeanRegistrations() {
    for (BeanRegistrationConfig regConfig : config.getBeanRegistrations()) {
      try {
        Path targetFile = Paths.get(regConfig.getTargetFile());
        if (!Files.exists(targetFile)) {
          System.err.println("Target file does not exist: " + targetFile);
          continue;
        }

        // 读取目标文件内容
        String content = new String(Files.readAllBytes(targetFile));

        // 准备要插入的Bean注册内容
        Map<String, String> beanContents = prepareBeanContent(regConfig.getBeanTemplates());

        // 在指定位置插入Bean注册
        // 在指定位置插入Bean注册
        String newContent;
        if (BeanFormatType.PROPERTY == regConfig.getFormatType()) {
          if (regConfig.getMapPropertyName() != null) {
            newContent = insertXmlMapEntry(content, beanContents, regConfig.getMapPropertyName());
          } else {
            newContent = insertXmlBeanRegistration(content, regConfig.getInsertPattern(),
                    beanContents.get(BeanRegistrationConfig.NEW_BEAN_DEFAULT_ID));
          }
        } else if (BeanFormatType.SERVER_IMPL == regConfig.getFormatType()) {
          newContent = insertBeanRegistration(content, regConfig.getInsertPattern(),
                  beanContents.get(BeanRegistrationConfig.NEW_BEAN_DEFAULT_ID),
                  regConfig.isInsertBefore());
        } else if (BeanFormatType.CONSTANT_JS == regConfig.getFormatType()){
          newContent = insertJsModule(content, beanContents.get(BeanRegistrationConfig.NEW_BEAN_DEFAULT_ID));
        } else {
          throw new IllegalArgumentException("Unsupported bean format type: " + regConfig.getFormatType());
        }

        // 写回文件
        Files.write(targetFile, newContent.getBytes());

        logger.info("Bean registration added to: " + targetFile);
      } catch (IOException e) {
        logger.error("Error processing bean registration for: " + regConfig.getTargetFile());
        throw new RuntimeException(e);
      }
    }
  }

  private String insertJsModule(String content, String beanContext) {
    // 按行分割内容
    String[] lines = content.split("\n");
    List<String> lineList = new ArrayList<>(Arrays.asList(lines));

    // 查找 MODULE 对象的起始位置
    int moduleStartIndex = -1;
    for (int i = 0; i < lineList.size(); i++) {
      if (lineList.get(i).trim().equals("MODULE: {")) {
        moduleStartIndex = i;
        break;
      }
    }

    if (moduleStartIndex == -1) {
      throw new IllegalArgumentException("未找到 MODULE 对象");
    }

    // 查找最后一个属性的结束位置（即最后一个 `},` 或 `}`）
    int lastPropertyEndIndex = -1;
    for (int i = lineList.size() - 1; i >= moduleStartIndex; i--) {
      String line = lineList.get(i).trim();
      if (line.equals("}")) {
        lastPropertyEndIndex = i;
        break;
      }
    }

    if (lastPropertyEndIndex == -1) {
      throw new IllegalArgumentException("未找到 MODULE 的最后一个属性结束位置");
    }

    // 继续往上找第一个 `}` 或 `},` 的行
    int firstBraceBeforeLast = -1;
    for (int i = lastPropertyEndIndex - 1; i >= moduleStartIndex; i--) {
      String line = lineList.get(i).trim();
      if (line.equals("}") || line.equals("},")) {
        firstBraceBeforeLast = i;
        break;
      }
    }

    // 如果没有找到更早的 `}` 或 `},`，则使用最后一个 `}` 或 `},` 的行
    int insertPosition = (firstBraceBeforeLast != -1) ? firstBraceBeforeLast : lastPropertyEndIndex;

    // 检查插入位置的行是否是 `}`，如果是则修改为 `},`
    String lineAtInsertPosition = lineList.get(insertPosition).trim();
    if (lineAtInsertPosition.equals("}")) {
      lineList.set(insertPosition, lineList.get(insertPosition).replace("}", "},"));
    }

    // 插入新模块（在最后一个属性的结束行之后）
    lineList.add(lastPropertyEndIndex, beanContext);

    // 重新组合为字符串
    return String.join("\n", lineList);
  }

  private String insertXmlBeanRegistration(String content, String pattern, String beanContent) {
    // 匹配</beans>标签
    Pattern regex = Pattern.compile(pattern);
    Matcher matcher = regex.matcher(content);

    if (matcher.find()) {
      // 在</beans>标签之前插入Bean配置
      int position = matcher.start();
      return content.substring(0, position) +
              beanContent +
              content.substring(position);
    }

    return content;
  }

  private String insertBeanRegistration(String content, String pattern, String beanContent, boolean insertBefore) {
    Pattern regex = Pattern.compile(pattern);
    Matcher matcher = regex.matcher(content);

    if (matcher.find()) {
      int position = insertBefore ? matcher.start() : matcher.end();
      return content.substring(0, position) + beanContent + content.substring(position);
    }

    return content;
  }

  private Map<String, String> prepareBeanContent(Map<String, String> templates) {
    Map<String, String> result = new HashMap<>();
    for(String key : templates.keySet()) {
      String template = templates.get(key);
      String content = template;
      // 替换模板中的模块ID
      for (Map.Entry<String, String> entry : config.getCaseVariations().entrySet()) {
        content = content.replace(entry.getKey(), entry.getValue());
      }
      result.put(key, content);
    }

    return result;
  }

  private String insertXmlMapEntry(String content, Map<String, String> beanTemplates, String propertyName) {

    StringBuilder result = new StringBuilder(content);
    for(String beanId : beanTemplates.keySet()) {
      String beanTemplate = beanTemplates.get(beanId);
      String patternStr = String.format("<bean[^>]*id=\"%s\"[^>]*>[\\s\\S]*?<property\\s+name=\"%s\">\\s*<map>",
              beanId, propertyName);
      Pattern pattern = Pattern.compile(patternStr);
      Matcher matcher = pattern.matcher(result.toString());

      int offset = 0; // 用于跟踪插入位置的变化
      while (matcher.find()) {
          // 找到map标签的开始位置
          int mapStart = matcher.end();

          // 查找map标签的结束位置
          int mapEnd = findMapEndPosition(result.toString(), mapStart + offset);
          if (mapEnd > 0) {
            // 查找最后一个entry的位置
            int lastEntryEnd = findLastEntryPosition(result.toString(), mapStart + offset, mapEnd + offset);
            if (lastEntryEnd > 0) {
              // 在最后一个entry之后插入新的entry
              result.insert(lastEntryEnd + offset, beanTemplate);
              offset += beanTemplate.length(); // 更新偏移量
            } else {
              // 如果没有entry，直接在map开始后插入
              result.insert(mapStart + offset, beanTemplate);
              offset += beanTemplate.length(); // 更新偏移量
            }
          }
        }
    }

    return result.toString();
  }

  private String extractBeanId(String content, int startPos) {
    // 提取bean的id属性
    Pattern idPattern = Pattern.compile("id=\"([^\"]+)\"");
    Matcher idMatcher = idPattern.matcher(content);
    if (idMatcher.find(startPos)) {
      return idMatcher.group(1);
    }
    return null;
  }

  private int findMapEndPosition(String content, int startPos) {
    int depth = 1;
    int pos = startPos;

    while (pos < content.length()) {
      if (content.substring(pos).startsWith("<map>")) {
        depth++;
        pos += 5;
      } else if (content.substring(pos).startsWith("</map>")) {
        depth--;
        if (depth == 0) {
          return pos;
        }
        pos += 6;
      } else {
        pos++;
      }
    }

    return -1;
  }

  private int findLastEntryPosition(String content, int mapStart, int mapEnd) {
    // 查找最后一个entry的结束位置
    Pattern entryPattern = Pattern.compile("<entry[^>]*>");
    Matcher entryMatcher = entryPattern.matcher(content);
    int lastEntryEnd = -1;
    int currentPos = mapStart;

    while (currentPos < mapEnd) {
      if (entryMatcher.find(currentPos) && entryMatcher.end() < mapEnd) {
        lastEntryEnd = entryMatcher.end();
        currentPos = entryMatcher.end(); // 更新当前位置
      } else {
        break; // 如果没有找到更多entry，退出循环
      }
    }

    return lastEntryEnd;
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