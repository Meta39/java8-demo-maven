package com.fu.velocitygeneratecodedemo.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件相关操作工具类
 */
public class FileUtils {
    private FileUtils() {
    }

    /**
     * 获取相对路径文件夹下的全部文件的文件名
     *
     * @param relativePath 模板存放相对路径
     */
    public static List<String> getResourcesFileName(String relativePath) {
        if (relativePath == null || relativePath.trim().isEmpty()) {
            throw new RuntimeException("The resource folder Relative path cannot be empty.");
        }
        URL resourceUrl = FileUtils.class.getClassLoader().getResource(relativePath);
        if (resourceUrl == null) {
            throw new RuntimeException("There are no files in the resource folder or the resource folder path does not exist.");
        }
        File file = new File(resourceUrl.getFile());
        File[] files = file.listFiles();
        if (files == null) {
            throw new RuntimeException("There are no files in the resource folder");
        }
        List<String> fileNameList = new ArrayList<>();
        for (File listFile : files) {
            fileNameList.add(listFile.getName());
        }
        return fileNameList;
    }

    /**
     * 判断文件名列表是否包含某个文件名称，如果是则返回对应的文件名称，没有则抛出异常。
     *
     * @param relativePath 模板存放相对路径
     * @param fileName     模板名称
     */
    public static String containsFile(String relativePath, String fileName) {
        if (getResourcesFileName(relativePath).contains(fileName)) {
            return fileName;
        }
        throw new RuntimeException("cannot find file : " + fileName);
    }

}