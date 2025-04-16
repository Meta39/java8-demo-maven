package com.fu.mybatissqllog.plugin;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Intercepts({
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class,}),
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})
})
public final class MyBatisSqlParsingPlugin implements Interceptor {
    private static final Logger log = LoggerFactory.getLogger(MyBatisSqlParsingPlugin.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        ParameterHandler parameterHandler = statementHandler.getParameterHandler();
        BoundSql boundSql = statementHandler.getBoundSql();
        try {
            String sql = formatSql(parameterHandler, boundSql);
            if (!boundSql.getSql().equals(sql)) {
                log.info("Execute SQL: {}", sql);
            }
        } catch (Exception e) {
            String sql = boundSql.getSql();
            log.error("SQL: {}", sql);
            log.error("formatSql Exception: ", e);
        }
        return invocation.proceed();
    }

    /**
     * 格式化SQL及其参数
     */
    private String formatSql(ParameterHandler parameterHandler, BoundSql boundSql) throws NoSuchFieldException, IllegalAccessException {

        Class<? extends ParameterHandler> parameterHandlerClass = parameterHandler.getClass();
        Field mappedStatementField = parameterHandlerClass.getDeclaredField("mappedStatement");
        mappedStatementField.setAccessible(true);
        MappedStatement mappedStatement = (MappedStatement) mappedStatementField.get(parameterHandler);

        String sql = boundSql.getSql().replaceAll("\\s+", " ");

        // sql字符串是空或存储过程，直接跳过
        if (!StringUtils.hasText(sql) || sql.trim().charAt(0) == '{') {
            return "";
        }

        // 不传参数的场景，直接把Sql美化一下返回出去
        Object parameterObject = parameterHandler.getParameterObject();
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappings();
        if (Objects.isNull(parameterObject) || parameterMappingList.isEmpty()) {
            return sql;
        }

        return handleCommonParameter(sql, boundSql, mappedStatement);
    }

    //替换预编译SQL
    private String handleCommonParameter(String sql, BoundSql boundSql, MappedStatement mappedStatement) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        Configuration configuration = mappedStatement.getConfiguration();
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        List<String> params = new ArrayList<>();

        for (ParameterMapping parameterMapping : parameterMappings) {
            if (parameterMapping.getMode() == ParameterMode.OUT) {
                continue;
            }
            Object propertyValue;
            String propertyName = parameterMapping.getProperty();
            if (boundSql.hasAdditionalParameter(propertyName)) {
                propertyValue = boundSql.getAdditionalParameter(propertyName);
            } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                propertyValue = parameterObject;
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                propertyValue = metaObject.getValue(propertyName);
            }
            params.add(this.formatParam(propertyValue));
        }

        //转译百分号
        if (sql.contains("%")) {
            //如果参数不一致直接返回SQL
            Pattern pattern = Pattern.compile("\\?");
            Matcher matcher = pattern.matcher(sql);
            int count = 0;
            while (matcher.find()) {
                count++;
            }
            if (count == 0 || params.isEmpty()) {
                return sql;
            }
            if (params.size() != count) {
                log.error("params.size() != count SQL: {}", sql);
                log.error("params.size() != count SQL parameters: {}", params);
                return sql;
            }
            sql = sql.replaceAll("%", "%%");
        }

        sql = sql.replaceAll("\\?", "%s");
        return String.format(sql, params.toArray());
    }

    private String formatParam(Object object) {
        if (object == null) {
            return "null";
        }
        // 尝试格式化String
        if (object instanceof String) {
            return formatString((String) object);
        }
        // 尝试格式化Date
        if (object instanceof Date) {
            return formatDate((Date) object);
        }
        // 尝试格式化LocalDate
        if (object instanceof LocalDate) {
            return formatLocalDate((LocalDate) object);
        }
        // 尝试格式化LocalDateTime
        if (object instanceof LocalDateTime) {
            return formatLocalDateTime((LocalDateTime) object);
        }
        // 默认行为：直接转换为字符串
        return object.toString();
    }

    private static String formatString(String str) {
        return "'" + str + "'";
    }

    private String formatDate(Date date) {
        return "'" + DATE_TIME_FORMATTER.format(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()) + "'";
    }

    private String formatLocalDate(LocalDate date) {
        return "'" + DATE_FORMATTER.format(date) + "'";
    }

    private String formatLocalDateTime(LocalDateTime dateTime) {
        return "'" + DATE_TIME_FORMATTER.format(dateTime) + "'";
    }

}