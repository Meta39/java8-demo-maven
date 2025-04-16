package com.fu.basedemo.io;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Objects;

/**
 * 磁盘操作
 */
public class JavaFileReadTest {

    /**
     * File 类可以用于表示文件和目录的信息，但是它不表示文件的内容。
     * 从 Java7 开始，可以使用 Paths 和 Files 代替 File。
     */
    @Test
    public void test() {
        //获取文件或目录
        File dir = new File("E:/File");
        showAllFiles(dir);
    }

    public void showAllFiles(File dir){
        if (dir == null || !dir.exists()) {
            return;
        }
        if (dir.isFile()) {
            System.out.println("文件名称：" + dir.getName());
            return;
        }
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            showAllFiles(file);
        }
    }
}
