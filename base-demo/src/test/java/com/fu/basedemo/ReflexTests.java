package com.fu.basedemo;

import com.fu.basedemo.designpattern.singleton.DoubleCheckedLockingParam;
import com.fu.basedemo.reflex.DoubleCheckedLockingParamWrapper;
import com.fu.basedemo.reflex.Reflex;
import com.fu.basedemo.reflex.ReflexEnum;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflexTests {

    @Test
    public void test() {
        System.out.println(ReflexEnum.value(ReflexEnum.ReflexA));
    }

    @Test
    void test2() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //1、获取类信息
        Class<?> clazz = Class.forName("com.fu.basedemo.reflex.Reflex");
        Reflex reflex = (Reflex) clazz.newInstance();
        // 获取指定方法
        Method method = clazz.getMethod("setName", String.class);
        // 调用方法
        method.invoke(reflex, "Meta");
        System.out.println("method调用Reflex.setName()：" + reflex);
    }

    /**
     * 单例模式双重检查+锁
     */
    @Test
    void test3() {
        DoubleCheckedLockingParam doubleCheckedLockingParam = DoubleCheckedLockingParam.getInstance("Meta", "123456", "1.0.0");
        DoubleCheckedLockingParam doubleCheckedLockingParam2 = DoubleCheckedLockingParam.getInstance("Meta2", "1234567", "2.0.0");
        //输出结果都是doubleCheckedLockingParam
        System.out.println(doubleCheckedLockingParam);
        System.out.println(doubleCheckedLockingParam2);
    }

    /**
     * 反射破坏单例模式
     * 性能最低
     */
    @Test
    void test4() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //通过反射修改单例模式的双重检查锁
        //1.获取私有构造函数
        Constructor<DoubleCheckedLockingParam> declaredConstructor = DoubleCheckedLockingParam.class.getDeclaredConstructor(String.class, String.class, String.class);
        //2.设置可访问
        declaredConstructor.setAccessible(true);
        //3.创建实例
        DoubleCheckedLockingParam doubleCheckedLockingParam = declaredConstructor.newInstance("Meta", "123456", "1.0.0");
        DoubleCheckedLockingParam doubleCheckedLockingParam2 = declaredConstructor.newInstance("Meta2", "1234567", "2.0.0");
        //破坏了单例模式，所以输出结果是不一样的。
        System.out.println(doubleCheckedLockingParam);
        System.out.println(doubleCheckedLockingParam2);
    }

    /**
     * 反射破坏单例模式2（Java 7+，性能更好）
     * 性能比Constructor.newInstance()高
     */
    @Test
    void test5() throws Throwable {
        //1.获取私有构造函数
        Constructor<DoubleCheckedLockingParam> declaredConstructor = DoubleCheckedLockingParam.class.getDeclaredConstructor(String.class, String.class, String.class);
        //2.设置可访问
        declaredConstructor.setAccessible(true);
        //3.转换为 MethodHandle
        MethodHandle methodHandle = MethodHandles.lookup().unreflectConstructor(declaredConstructor);
        //4.通过 MethodHandle 创建实例（invokeExact性能比invokeWithArguments高，但invokeExact确保参数类型完全匹配）
        //DoubleCheckedLockingParam doubleCheckedLockingParam = (DoubleCheckedLockingParam)methodHandle.invokeWithArguments("Meta", "123456", "1.0.0");
        DoubleCheckedLockingParam doubleCheckedLockingParam = (DoubleCheckedLockingParam) methodHandle.invokeExact("Meta", "123456", "1.0.0");
        DoubleCheckedLockingParam doubleCheckedLockingParam2 = (DoubleCheckedLockingParam) methodHandle.invokeExact("Meta2", "1234567", "2.0.0");
        //破坏了单例模式，所以输出结果是不一样的。
        System.out.println(doubleCheckedLockingParam);
        System.out.println(doubleCheckedLockingParam2);
    }

    @Test
    void test6() {
        //1.缓存没有实例，则进行创建
        DoubleCheckedLockingParam doubleCheckedLockingParam1 = DoubleCheckedLockingParamWrapper.getInstance("Meta", "123456", "1.0.0");
        //2.因为绕过私有访问，所以可以创建多实例
        DoubleCheckedLockingParam doubleCheckedLockingParam2 = DoubleCheckedLockingParamWrapper.getInstance("Meta2", "1234567", "2.0.0");
        //3.因为缓存了 doubleCheckedLockingParam1 所以 doubleCheckedLockingParam3 是直接从缓存取的。
        DoubleCheckedLockingParam doubleCheckedLockingParam3 = DoubleCheckedLockingParamWrapper.getInstance("Meta", "123456", "1.0.0");
        System.out.println(doubleCheckedLockingParam1 == doubleCheckedLockingParam2);//false。因为是2个不同的实例，不是单例
        System.out.println(doubleCheckedLockingParam1 == doubleCheckedLockingParam3);//true。因为是从缓存获取的同一个实例
    }

}