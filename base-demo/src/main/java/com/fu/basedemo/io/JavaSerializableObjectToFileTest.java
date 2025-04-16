package com.fu.basedemo.io;

import lombok.Data;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 对象操作
 */
public class JavaSerializableObjectToFileTest {

    /**
     * 序列化对象到文件
     */
    @Test
    @SneakyThrows
    public void saveObjectToFile() {
        Obj writeObj = new Obj(123456, "abc","不会被序列化和反序列化");
        String objectFile = "E:/File/object.txt";

        Path path = Paths.get(objectFile);
        //写入内容到文件（序列化）
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(path));
        objectOutputStream.writeObject(writeObj);
        objectOutputStream.close();

        //读取文件内容（反序列化）
        ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(path));
        Obj readObj = (Obj) objectInputStream.readObject();
        objectInputStream.close();
        System.out.println("读取Obj：" + readObj);
    }

    /**
     * 序列化的类需要实现 Serializable 接口，它只是一个标准，没有任何方法需要实现，但是如果不去实现它的话而进行序列化，会抛出异常。
     * transient 关键字可以使一些属性不会被序列化。
     */
    @Data
    private static class Obj implements Serializable {

        private static final long serialVersionUID = -7438683541197384672L;
        private final int x;
        private final String y;
        private final transient String z;
    }
}


