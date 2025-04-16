package com.fu.basedemo;

import com.fu.basedemo.designpattern.singleton.*;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 单例模式测试线程安全、反射攻击、反序列化
 * PS：还有枚举类，因为枚举类本身就是线程安全、防止反射攻击、防止反序列化，所以这里不在赘述。
 *
 * @since 2024-07-25
 */
public class SingletonTests {

    /**
     * 懒汉式静态内部类单例测试
     * 测试结果：
     * singleton1 == singleton2: true（线程安全）
     * singleton1 == singleton3: true（有效防止反序列化）
     */
    @Test
    public void billPughSingletonDesignTest() throws IOException {
        // 验证线程安全性
        BillPughSingletonDesign singleton1 = BillPughSingletonDesign.getInstance();

        BillPughSingletonDesign singleton2 = BillPughSingletonDesign.getInstance();
        System.out.println("singleton1 == singleton2: " + (singleton1 == singleton2)); // 应该输出true

        // 验证防止反序列化
        Path path = Paths.get("singleton.txt");
        try (ObjectOutput out = new ObjectOutputStream(Files.newOutputStream(path))) {
            out.writeObject(BillPughSingletonDesign.getInstance());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BillPughSingletonDesign singleton3;
        try (ObjectInput in = new ObjectInputStream(Files.newInputStream(path))) {
            singleton3 = (BillPughSingletonDesign) in.readObject();
            Files.delete(path); // 删除文件
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("singleton1 == singleton3: " + (singleton1 == singleton3)); // 应该输出true
    }

    /**
     * 懒汉式静态内部类单例反射测试
     * 结论：无法防止反射攻击
     * singleton1 == singleton4: false
     */
    @Test
    public void billPughSingletonDesignReflexTest() {
        // 验证防止反射攻击
        try {
            // 获取类的Class对象
            Class<BillPughSingletonDesign> clazz = BillPughSingletonDesign.class;

            // 获取私有构造函数
            Constructor<BillPughSingletonDesign> declaredConstructor = clazz.getDeclaredConstructor();

            // 设置可访问（可以让私有化构造函数无效）
            declaredConstructor.setAccessible(true);

            // 创建实例
            BillPughSingletonDesign singleton1 = declaredConstructor.newInstance();
            BillPughSingletonDesign singleton4 = declaredConstructor.newInstance();
            System.out.println("singleton1 == singleton4: " + (singleton1 == singleton4));
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 懒汉式双重检查锁定单例测试
     * 测试结果：
     * singleton1 == singleton2: true（线程安全）
     * singleton1 == singleton3: true（无法防止反序列化）
     */
    @Test
    public void doubleCheckedLockingTest() throws IOException {
        // 验证线程安全性
        DoubleCheckedLocking singleton1 = DoubleCheckedLocking.getInstance();
        DoubleCheckedLocking singleton2 = DoubleCheckedLocking.getInstance();
        System.out.println("singleton1 == singleton2: " + (singleton1 == singleton2)); // 应该输出true

        // 验证防止反序列化
        Path path = Paths.get("singleton.txt");
        try (ObjectOutput out = new ObjectOutputStream(Files.newOutputStream(path))) {
            out.writeObject(DoubleCheckedLocking.getInstance());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DoubleCheckedLocking singleton3;
        try (ObjectInput in = new ObjectInputStream(Files.newInputStream(path))) {
            singleton3 = (DoubleCheckedLocking) in.readObject();
            Files.delete(path); // 删除文件
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println("singleton1 == singleton3: " + (singleton1 == singleton3)); // 应该输出true
    }

    /**
     * 懒汉式双重检查锁定单例反射测试
     * 结论：无法防止反射攻击
     * singleton1 == singleton2: false
     */
    @Test
    public void doubleCheckedLockingReflexTest() {
        // 验证防止反射攻击
        try {
            // 获取类的Class对象
            Class<DoubleCheckedLocking> clazz = DoubleCheckedLocking.class;

            // 获取私有构造函数
            Constructor<DoubleCheckedLocking> declaredConstructor = clazz.getDeclaredConstructor();

            // 设置可访问（可以让私有化构造函数无效）
            declaredConstructor.setAccessible(true);

            // 创建实例
            DoubleCheckedLocking singleton1 = declaredConstructor.newInstance();
            DoubleCheckedLocking singleton2 = declaredConstructor.newInstance();
            System.out.println("singleton1 == singleton2: " + (singleton1 == singleton2)); // 不应该执行到这里
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * （带参数）懒汉式双重检查锁定单例测试
     * singleton1:DoubleCheckedLockingParam{username='Meta', password='123456', version='1.0'}
     * singleton2:DoubleCheckedLockingParam{username='Meta', password='123456', version='1.0'}
     * singleton1 == singleton2: true
     */
    @Test
    public void doubleCheckedLockingParamTest() {
        // 验证线程安全性
        DoubleCheckedLockingParam singleton1 = DoubleCheckedLockingParam.getInstance("Meta", "123456", "1.0");
        DoubleCheckedLockingParam singleton2 = DoubleCheckedLockingParam.getInstance("Meta2", "234567", "2.0");
        System.out.println("singleton1:" + singleton1.toString());
        System.out.println("singleton2:" + singleton2.toString());
        System.out.println("singleton1 == singleton2: " + (singleton1 == singleton2)); // 应该输出true
    }

    /**
     * （带参数）懒汉式双重检查锁定单例防止反射测试
     * 结论：无法防止反射攻击
     * 第一次创建实例:DoubleCheckedLockingParam{username='Meta2', password='234567', version='2.0'}
     * 第二次创建实例:DoubleCheckedLockingParam{username='Meta3', password='345678', version='3.0'}
     */
    @Test
    public void doubleCheckedLockingParamReflexTest() {
        try {
            // 获取ParameterizedSingleton类的Class对象
            Class<?> clazz = DoubleCheckedLockingParam.class;

            // 获取私有构造方法
            Constructor<?> constructor = clazz.getDeclaredConstructor(String.class, String.class, String.class);

            // 设置构造方法为可访问
            constructor.setAccessible(true);

            // 第一次通过反射创建实例
            DoubleCheckedLockingParam instance1 = (DoubleCheckedLockingParam) constructor.newInstance("Meta2", "234567", "2.0");
            System.out.println("第一次创建实例:" + instance1);

            // 第二次通过反射创建实例，预期会抛出异常
            DoubleCheckedLockingParam instance2 = (DoubleCheckedLockingParam) constructor.newInstance("Meta3", "345678", "3.0");
            System.out.println("第二次创建实例:" + instance2);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 饿汉式单例测试
     * 测试结果：
     * singleton1 == singleton2: true（线程安全）
     * singleton1 == singleton3: true（有效防止反序列化）
     */
    @Test
    public void eagerInitializationTest() throws IOException {
        // 验证线程安全性
        EagerInitialization singleton1 = EagerInitialization.getInstance();
        EagerInitialization singleton2 = EagerInitialization.getInstance();
        System.out.println("singleton1 == singleton2: " + (singleton1 == singleton2)); // 应该输出true

        // 验证防止反序列化
        Path path = Paths.get("singleton.txt");
        try (ObjectOutput out = new ObjectOutputStream(Files.newOutputStream(path))) {
            out.writeObject(EagerInitialization.getInstance());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        EagerInitialization singleton3;
        try (ObjectInput in = new ObjectInputStream(Files.newInputStream(path))) {
            singleton3 = (EagerInitialization) in.readObject();
            Files.delete(path); // 删除文件
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("singleton1 == singleton3: " + (singleton1 == singleton3)); // 应该输出true
    }

    @Test
    public void eagerInitializationReflexTest() {
        // 验证防止反射攻击
        try {
            // 获取类的Class对象
            Class<EagerInitialization> clazz = EagerInitialization.class;

            // 获取私有构造函数
            Constructor<EagerInitialization> declaredConstructor = clazz.getDeclaredConstructor();

            // 设置可访问（可以让私有化构造函数无效）
            declaredConstructor.setAccessible(true);

            // 创建实例
            EagerInitialization singleton1 = declaredConstructor.newInstance();
            EagerInitialization singleton4 = declaredConstructor.newInstance();
            System.out.println("singleton1 == singleton4: " + (singleton1 == singleton4));
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void eagerInitializationParamTest() {
        EagerInitializationParam instance1 = EagerInitializationParam.getInstance();
        instance1.connect();
        EagerInitializationParam instance2 = EagerInitializationParam.getInstance();
        instance2.connect();

        System.out.println("instance1 == instance2: " + (instance1 == instance2));
    }


}
