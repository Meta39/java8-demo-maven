package com.fu.springbootdemo.util;


import java.util.concurrent.ThreadLocalRandom;

/**
 * 生成随机工具类
 */
public abstract class GeneratorRandomUtil {
    private static final String ALPHABETS_IN_UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHABETS_IN_LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";

    //私有化构造函数，防止其它人new一个GeneratorRandomUtil进行调用内部方法。如需新增方法，应当在此工具类创建公有静态方法调用实例化的私有静态方法。
    private GeneratorRandomUtil() {
    }

    /**
     * 获取固定长度的随机字符串和数字
     *
     * @param length 字符串长度
     */
    public static String getRandomStringAndNumbers(int length) {
        checkLength(length);
        return generatorRandomStringAndNumbers(length);
    }

    /**
     * 获取随机长度字符串和数字，可以用于密码盐salt，提供给前端进行加密。
     */
    public static String getRandomLengthStringAndNumbers() {
        return generatorRandomStringAndNumbers(ThreadLocalRandom.current().nextInt(10) + 1);//字符串长度1-10
    }

    /**
     * 获取固定长度的随机数字字符串。可以用于验证码
     */
    public static String getRandomNumbers(int length) {
        checkLength(length);
        return generatorRandomNumbers(length);
    }

    /**
     * 获取随机长度的随机数字字符串
     */
    public static String getRandomLengthNumbers() {
        return generatorRandomNumbers(ThreadLocalRandom.current().nextInt(10) + 1);//字符串长度1-10
    }

    //------------------------------------私有方法---------------------------------------------
    private static void checkLength(int length) {
        if (length < 1) {
            throw new RuntimeException("长度不能小于1");
        }
    }

    private static String generatorRandomNumbers(int length) {
        StringBuilder randomString = new StringBuilder();
        ThreadLocalRandom current = ThreadLocalRandom.current();
        for (int i = 0; i < length; i++) {
            randomString.append(NUMBERS.charAt(current.nextInt(NUMBERS.length())));
        }
        return randomString.toString();
    }

    private static String generatorRandomStringAndNumbers(int length) {
        String allCharacters = ALPHABETS_IN_LOWER_CASE + ALPHABETS_IN_UPPER_CASE + NUMBERS;
        StringBuilder randomString = new StringBuilder();
        ThreadLocalRandom current = ThreadLocalRandom.current();
        for (int i = 0; i < length; i++) {
            randomString.append(allCharacters.charAt(current.nextInt(allCharacters.length())));
        }
        return randomString.toString();
    }

}
