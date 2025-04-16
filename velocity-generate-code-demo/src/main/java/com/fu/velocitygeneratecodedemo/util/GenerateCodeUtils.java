package com.fu.velocitygeneratecodedemo.util;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class GenerateCodeUtils {
    private static final String PACKAGE_PATH = "main";
    private static final String JAVA = "java";
    private static final String RESOURCES = "resources";

    /**
     * 生成代码
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        Map<String, Object> data = new HashMap<>();
        data.put("vmPath", "mybatis-plus");
        data.put("classname", "user");
        data.put("className", "User");
        data.put("package", "com.fu.velocitygeneratecodedemo");
        data.put("resourcesFilePath", "mapper");
        data.put("saveFilePath", "D:/");
        generateCode(data);
    }

    /**
     * 生成代码
     */
    public static void generateCode(Map<String, Object> data) {
        //1.设置velocity资源加载器
        Properties properties = new Properties();
        properties.put("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        //2.初始化velocity引擎
        Velocity.init(properties);
        //3.创建velocity容器
        VelocityContext velocityContext = new VelocityContext(data);
        //遍历模板列表，获取每一个模板，基于每一个模板生成一个代码。
        String vmPath = data.get("vmPath").toString();
        for (String templateVM : FileUtils.getResourcesFileName(vmPath)) {
            //4.加载velocity模板文件
            Template template = Velocity.getTemplate(vmPath + File.separator + templateVM, "UTF-8");
            String fileName = getFileName(vmPath, templateVM, data.get("className").toString(), data.get("package").toString(), data.get("resourcesFilePath").toString());
            //合并代码把代码数据输出到文件夹中
            String generateFileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
            File file = new File(data.get("saveFilePath") + fileName.replace(generateFileName, ""));
            if (!file.exists()) {
                file.mkdirs();
            }
            try (FileWriter fileWriter = new FileWriter(data.get("saveFilePath") + fileName)) {
                template.merge(velocityContext, fileWriter);
            } catch (IOException e) {
                throw new RuntimeException("save file exception.", e);
            }
        }
    }

    /**
     * 根据模板名称、类名称、包名称拼接一个完整的文件路径和名称
     *
     * @param template    模板名称
     * @param className   类名称
     * @param packageName 包名称
     * @return main/java/com/fu/velocitygeneratecodedemo/controller/UserController.java 等...
     */
    public static String getFileName(String vmPath, String template, String className, String packageName, String resourcesFilePath) {
        String packagePath = PACKAGE_PATH + File.separator + JAVA + File.separator;//java文件
        String resourcesPath = PACKAGE_PATH + File.separator + RESOURCES + File.separator;//静态资源文件
        if (isNotBlank(packagePath)) {
            packagePath += packageName.replace(".", File.separator) + File.separator;
        }
        if (isNotBlank(resourcesFilePath)) {
            resourcesPath += resourcesFilePath.replace(".", File.separator) + File.separator;
        }

        String templateName = FileUtils.containsFile(vmPath, template);

        //java文件
        if (isNotBlank(templateName)) {
            String[] splitTemplate = templateName.split("\\.");
            String packageOrClassName = splitTemplate[0];
            if (templateName.contains(".java.vm")) {
                return packagePath + packageOrClassName.toLowerCase() + File.separator + className + packageOrClassName + "." + splitTemplate[1];
            } else {
                return resourcesPath + className + packageOrClassName + "." + splitTemplate[1];
            }
        }
        //静态资源文件放到resources目录下
        throw new RuntimeException("No templates found.");
    }

    /**
     * 判断字符串非空
     *
     * @param string 字符串
     */
    public static boolean isNotBlank(String string) {
        return string != null && string.trim().length() != 0;
    }

}
