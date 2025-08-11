package com.fu.mybatissqllog.plugin;

import com.fu.mybatissqllog.util.DateTimeUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Intercepts({
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class,}),
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})
})
public final class MyBatisSqlParsingPlugin implements Interceptor {
    private static final Logger log = LoggerFactory.getLogger(MyBatisSqlParsingPlugin.class);
    private static final String PERCENT_SIGN = "%";
    private static final String PERCENT_SIGN2 = PERCENT_SIGN + PERCENT_SIGN;//%%
    private static final String PERCENT_SIGN_STRING = PERCENT_SIGN + "s";//%s
    private static final String NULL_STRING = "null";//null字符串
    private static final String SPACE = " ";//空格
    private static final String APOSTROPHE = "'";//单引号'
    private static final char LEFT_CURLY_BRACES = '{';//左花括号
    private static final char QUESTION_MARK = '?';//问号字符
    private static final String TRANSLATION_QUESTION_MARK = "\\?";//转译问号
    private static final String REGEX_STRING = "\\s+";
    //注入 ApplicationContext 然后通过 SqlSessionFactory 获取 Configuration
    private final ApplicationContext applicationContext;

    public MyBatisSqlParsingPlugin(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (invocation.getTarget() instanceof StatementHandler) {
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
            ParameterHandler parameterHandler = statementHandler.getParameterHandler();
            BoundSql boundSql = statementHandler.getBoundSql();
            String sqlSource = boundSql.getSql();

            try {
                SqlSessionFactory sqlSessionFactory = applicationContext.getBean(SqlSessionFactory.class);
                Configuration configuration = sqlSessionFactory.getConfiguration();
                String sql = formatSql(boundSql, parameterHandler.getParameterObject(), configuration);
                if (!sqlSource.equals(sql)) {
                    log.info("{}", sql);
                }
            } catch (Exception e) {
                log.error("{}\nSqlParsingException:", sqlSource, e);
            }
        }
        return invocation.proceed();
    }

    /**
     * 格式化SQL及其参数
     */
    private String formatSql(BoundSql boundSql, Object parameterObject, Configuration configuration) {
        String sql = boundSql.getSql().replaceAll(REGEX_STRING, SPACE);

        // sql字符串是空或存储过程，直接跳过
        if (!StringUtils.hasText(sql) || sql.trim().charAt(0) == LEFT_CURLY_BRACES) {
            return SPACE;
        }

        // 不传参数的场景，直接把Sql美化一下返回出去
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappings();
        if (Objects.isNull(parameterObject) || parameterMappingList.isEmpty()) {
            return sql;
        }

        return handleCommonParameter(sql, boundSql, configuration);
    }

    //替换预编译SQL
    private String handleCommonParameter(String sql, BoundSql boundSql, Configuration configuration) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
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

        //如果参数和值不一致直接返回SQL
        int count = 0;
        for (int i = 0; i < sql.length(); i++) {
            if (sql.charAt(i) == QUESTION_MARK) {
                count++;
            }
        }
        if (count == 0 || params.isEmpty()) {
            return sql;
        }
        if (params.size() != count) {
            //SQL 参数和参数值长度不一致
            log.error("SQL parameter length and value are inconsistent, SQL:{}\nSQL parameters:{}", sql, params);
            return sql;
        }

        //转译百分号
        if (sql.contains(PERCENT_SIGN)) {
            sql = sql.replaceAll(PERCENT_SIGN, PERCENT_SIGN2);
        }

        sql = sql.replaceAll(TRANSLATION_QUESTION_MARK, PERCENT_SIGN_STRING);
        return String.format(sql, params.toArray());
    }

    private String formatParam(Object object) {
        if (object == null) {
            return NULL_STRING;
        }
        if (object instanceof String) {
            return formatString((String) object);
        }
        if (object instanceof Date) {
            return formatDate((Date) object);
        }
        if (object instanceof LocalDate) {
            return formatLocalDate((LocalDate) object);
        }
        if (object instanceof LocalDateTime) {
            return formatLocalDateTime((LocalDateTime) object);
        }
        return object.toString();
    }

    private static String formatString(String str) {
        return APOSTROPHE + str + APOSTROPHE;
    }

    private String formatDate(Date date) {
        return APOSTROPHE + DateTimeUtils.dateToString(date) + APOSTROPHE;
    }

    private String formatLocalDate(LocalDate date) {
        return APOSTROPHE + DateTimeUtils.localDateToString(date) + APOSTROPHE;
    }

    private String formatLocalDateTime(LocalDateTime dateTime) {
        return APOSTROPHE + DateTimeUtils.localDateTimeToString(dateTime) + APOSTROPHE;
    }

}