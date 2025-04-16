package com.fu.basedemo.utils;

import com.fu.basedemo.annotation.Column;
import com.fu.basedemo.annotation.IgnoreColumn;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @since 2024-07-25
 */
public class ResultSetMapperUtils {

    private ResultSetMapperUtils() {
    }

    public static <T> T resultSetToObject(ResultSet resultSet, Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(IgnoreColumn.class)) {
                    continue; // 跳过被 @IgnoreColumn 注解标记的字段
                }
                String columnName = field.getName(); // 默认使用字段名称
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    if (!column.value().isEmpty()) {
                        columnName = column.value();
                    }
                }

                Object value = resultSet.getObject(columnName);

                field.setAccessible(true);
                field.set(instance, value);
            }
            return instance;
        } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> String buildSelectQuery(Class<T> clazz) {
        StringBuilder query = new StringBuilder("SELECT ");

        Field[] fields = clazz.getDeclaredFields();
        boolean first = true;

        for (Field field : fields) {
            if (field.isAnnotationPresent(IgnoreColumn.class)) {
                continue; // 跳过被 @Ignore 注解标记的字段
            }

            if (!first) {
                query.append(", ");
            }
            query.append(field.getName()); // 使用字段名称作为列名
            first = false;
        }

        query.append(" FROM ");
        query.append(clazz.getSimpleName().toLowerCase()); // 使用类名小写作为表名

        return query.toString();
    }

}
