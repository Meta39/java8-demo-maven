package com.fu.basedemo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 应用字符串工具类
 */
public class AppStringUtils {

    /**
     * 对包含数值字符串进行递增操作。
     * 例子：0、000000000001、Abc000000000001
     * @param original 原始包含数值字符串
     * @return 递增后的数值字符串
     */
    public static String incrementNumberInStringRegex(String original) {
        Pattern pattern = Pattern.compile("^(.*?)(\\d+)$"); // 匹配前缀（非贪婪）和尾部的数字
        Matcher matcher = pattern.matcher(original);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("发票号码段维护错误！请使用：Abc00000000001或00000000001这种例子的发票号码！");
        }
        String prefix = matcher.group(1);
        String numberStr = matcher.group(2);

        long number = Long.parseLong(numberStr);
        number++;

        String incrementedNumberStr = String.format("%0" + numberStr.length() + "d", number);

        return prefix + incrementedNumberStr;
    }

}
