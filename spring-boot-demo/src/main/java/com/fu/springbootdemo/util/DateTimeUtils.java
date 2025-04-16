package com.fu.springbootdemo.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日期时间工具类
 *
 * @since 2024-07-05
 */
public abstract class DateTimeUtils {
    private DateTimeUtils() {
    }

    // 自定义格式
    public static final String yyyy = "yyyy";
    public static final String MM = "MM";
    public static final String dd = "dd";
    public static final String HH = "HH";
    public static final String mm = "mm";
    public static final String ss = "ss";
    public static final String DIY_TIME_FORMAT_NO_SECOND = "HH:mm";
    public static final String DIY_TIME_FORMAT = "HHmmss";
    public static final String DIY_DATE_FORMAT = "yyyyMMdd";
    public static final String DIY_DATE_TIME_FORMAT = "yyyyMMddHHmmss";

    // 默认格式
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // 缓存DateTimeFormatter实例
    private static final Map<String, DateTimeFormatter> formatterCache = new ConcurrentHashMap<>();

    public static DateTimeFormatter getFormatter(String format) {
        return formatterCache.computeIfAbsent(format, DateTimeFormatter::ofPattern);
    }

    // 自动补全格式字符串中的缺失部分
    private static String autoCompleteFormat(String format) {
        if (!format.contains(HH)) {
            format += " HH";  // 补全小时
        }
        if (!format.contains(mm)) {
            format += ":mm";  // 补全分钟
        }
        if (!format.contains(ss)) {
            format += ":ss";  // 补全秒
        }
        return format;
    }

    // 自动补全时间字符串中的缺失部分
    private static String autoCompleteDateTimeString(String dateTimeStr, String format) {
        // 判断输入是否包含日期部分（yyyy 或者 MM 或者 dd）
        boolean containsDate = format.contains(yyyy) || format.contains(MM) || format.contains(dd);

        // 如果输入的字符串只有时间部分（没有空格分割的日期部分）
        if (!containsDate && !dateTimeStr.contains(" ")) {
            return dateTimeStr;  // 不进行任何补全
        }

        // 分离日期和时间部分
        String[] dateTimeParts = dateTimeStr.split(" ");
        String datePart = dateTimeParts[0];  // 日期部分
        String timePart = dateTimeParts.length > 1 ? dateTimeParts[1] : "";  // 时间部分

        // 补全逻辑
        if (format.contains(HH) && timePart.isEmpty()) {
            timePart = "00";  // 默认补全小时为00
        }

        // 如果格式包含分钟并且时间部分少于5位（表示缺少分钟部分）
        if (format.contains(mm) && timePart.length() == 2) {
            timePart += ":00";  // 补全分钟
        }

        // 如果格式包含秒并且时间部分少于8位（表示缺少秒部分）
        if (format.contains(ss) && timePart.length() == 5) {
            timePart += ":00";  // 补全秒
        }

        return datePart + (timePart.isEmpty() ? "" : " " + timePart);
    }

    // Date to LocalDate
    public static LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // Date to LocalTime
    public static LocalTime dateToLocalTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    // Date to LocalDateTime
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    // LocalDate to Date
    public static Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    // LocalTime to Date (current date with specified time)
    public static Date localTimeToDate(LocalTime localTime) {
        return Date.from(localTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
    }

    // LocalDateTime to Date
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    // String to LocalDate with default format
    public static LocalDate stringToLocalDate(String dateStr) {
        return stringToLocalDate(dateStr, DEFAULT_DATE_FORMAT);
    }

    // String to LocalDate with specified format
    public static LocalDate stringToLocalDate(String dateStr, String format) {
        DateTimeFormatter formatter = getFormatter(format);
        return LocalDate.parse(dateStr, formatter);
    }

    // String to LocalTime with default format
    public static LocalTime stringToLocalTime(String timeStr) {
        return stringToLocalTime(timeStr, DEFAULT_TIME_FORMAT);
    }

    // String to LocalTime with specified format
    public static LocalTime stringToLocalTime(String timeStr, String format) {
        DateTimeFormatter formatter = getFormatter(format);
        return LocalTime.parse(timeStr, formatter);
    }

    // String to LocalDateTime with default format
    public static LocalDateTime stringToLocalDateTime(String dateTimeStr) {
        return stringToLocalDateTime(dateTimeStr, DEFAULT_DATE_TIME_FORMAT);
    }

    // String to LocalDateTime with specified format
    public static LocalDateTime stringToLocalDateTime(String dateTimeStr, String format) {
        DateTimeFormatter formatter = getFormatter(format);
        return LocalDateTime.parse(dateTimeStr, formatter);
    }

    // LocalDate to String with default format
    public static String localDateToString(LocalDate localDate) {
        return localDateToString(localDate, DEFAULT_DATE_FORMAT);
    }

    // LocalDate to String with specified format
    public static String localDateToString(LocalDate localDate, String format) {
        DateTimeFormatter formatter = getFormatter(format);
        return localDate.format(formatter);
    }

    // LocalTime to String with default format
    public static String localTimeToString(LocalTime localTime) {
        return localTimeToString(localTime, DEFAULT_TIME_FORMAT);
    }

    // LocalTime to String with specified format
    public static String localTimeToString(LocalTime localTime, String format) {
        DateTimeFormatter formatter = getFormatter(format);
        return localTime.format(formatter);
    }

    // LocalDateTime to String with default format
    public static String localDateTimeToString(LocalDateTime localDateTime) {
        return localDateTimeToString(localDateTime, DEFAULT_DATE_TIME_FORMAT);
    }

    // LocalDateTime to String with specified format
    public static String localDateTimeToString(LocalDateTime localDateTime, String format) {
        DateTimeFormatter formatter = getFormatter(format);
        return localDateTime.format(formatter);
    }

    // String to Date with default format
    public static Date stringToDate(String dateStr) {
        return stringToDate(dateStr, DEFAULT_DATE_TIME_FORMAT);
    }

    /**
     * String to Date with specified format
     * @param dateStr 日期时间字符串
     * @param format 转换格式
     * @return date
     */
    public static Date stringToDate(String dateStr, String format) {
        DateTimeFormatter formatter = getFormatter(format);

        // 判断是否只包含年份
        if (format.contains(yyyy) && !format.contains(MM) && !format.contains(dd) && !(format.contains(HH) || format.contains(mm) || format.contains(ss))) {
            // 只包含年份，默认设置为该年的第一天
            LocalDate localDate = LocalDate.parse(dateStr + "-01-01", getFormatter(DEFAULT_DATE_FORMAT));
            return localDateToDate(localDate);
        }
        // 判断是否只包含年份和月份
        else if (format.contains(yyyy) && format.contains(MM) && !format.contains(dd) && !(format.contains(HH) || format.contains(mm) || format.contains(ss))) {
            // 只包含年份和月份，解析为 YearMonth
            YearMonth yearMonth = YearMonth.parse(dateStr, formatter);
            LocalDate localDate = yearMonth.atDay(1); // 默认第一天
            return localDateToDate(localDate);
        }
        // 判断是否只包含日期部分
        else if (format.contains(yyyy) && !format.contains(HH) && !format.contains(mm) && !format.contains(ss)) {
            // 只包含完整日期，解析为 LocalDate
            LocalDate localDate = LocalDate.parse(dateStr, formatter);
            return localDateToDate(localDate);
        }
        // 判断是否只包含时间部分
        else if (format.contains(HH) && !(format.contains(yyyy) || format.contains(MM) || format.contains(dd))) {
            // 只包含时间部分，解析为 LocalTime
            LocalTime localTime = LocalTime.parse(dateStr, formatter);
            LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), localTime);
            return localDateTimeToDate(localDateTime);
        } else {
            // 包含日期和时间，解析为 LocalDateTime
            String autoCompletedDateStr = autoCompleteDateTimeString(dateStr, format);
            LocalDateTime localDateTime = LocalDateTime.parse(autoCompletedDateStr, formatter);
            return localDateTimeToDate(localDateTime);
        }
    }


    // Date to String with default format
    public static String dateToString(Date date) {
        return dateToString(date, DEFAULT_DATE_TIME_FORMAT);
    }

    // Date to String with specified format
    public static String dateToString(Date date, String format) {
        DateTimeFormatter formatter = getFormatter(format);
        return formatter.format(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
    }

}