package com.fu.mybatissqllog.plugin;

import com.fu.mybatissqllog.util.DateTimeUtils;
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
    private static final String NULL_STRING = "null";//null字符串
    private static final String SPACE = " ";//空格
    private static final String APOSTROPHE = "'";//单引号'
    private static final char LEFT_CURLY_BRACES = '{';//左花括号
    private static final char QUESTION_MARK = '?';//问号字符
    private static final String QUESTION_MARK_STRING = "?";//问号字符
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
            BoundSql boundSql = statementHandler.getBoundSql();
            String sqlSource = boundSql.getSql();

            try {
                SqlSessionFactory sqlSessionFactory = applicationContext.getBean(SqlSessionFactory.class);
                Configuration configuration = sqlSessionFactory.getConfiguration();
                String sql = formatSql(boundSql, configuration);
                log.info("{}", sql);
            } catch (Exception e) {
                log.error("{}\nSqlParsingException:", sqlSource, e);
            }
        }
        return invocation.proceed();
    }

    /**
     * 格式化SQL及其参数
     */
    private String formatSql(BoundSql boundSql, Configuration configuration) {
        String sql = boundSql.getSql().replaceAll(REGEX_STRING, SPACE);

        // sql字符串是空或存储过程，直接跳过
        if (!StringUtils.hasText(sql) || sql.trim().charAt(0) == LEFT_CURLY_BRACES) {
            return SPACE;
        }

        // 不传参数的场景，直接把Sql返回出去
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (Objects.isNull(boundSql.getParameterObject())
                ||
                (Objects.isNull(parameterMappings) || parameterMappings.isEmpty())) {
            return sql;
        }

        return handleCommonParameter(sql, boundSql, configuration, parameterMappings);
    }

    //替换预编译SQL
    private String handleCommonParameter(String sql, BoundSql boundSql, Configuration configuration,List<ParameterMapping> parameterMappings) {
        Object parameterObject = boundSql.getParameterObject();
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

        if (params.isEmpty()) {
            return sql;
        }

        //如果参数和值不一致直接返回SQL
        int count = 0;
        for (int i = 0; i < sql.length(); i++) {
            if (sql.charAt(i) == QUESTION_MARK) {
                count++;
            }
        }
        if (count == 0) {
            return sql;
        }
        if (params.size() != count) {
            //SQL 参数和参数值长度不一致
            log.error("SQL parameter length and value are inconsistent, SQL:{}\nSQL parameters:{}", sql, params);
            return sql;
        }

        return finalSql(sql, params);
    }

    /**
     * 完整SQL
     * @param sql 预编译SQL
     * @param params 参数
     */
    private String finalSql(String sql, List<String> params) {
        StringBuilder sb = new StringBuilder(sql);
        int offset = 0; // 偏移量，因为替换后字符串长度会变化
        for (String param : params) {
            int index = sb.indexOf(QUESTION_MARK_STRING, offset);
            if (index == -1) {
                break; // 没有更多 ? 了
            }

            // 替换这个 ?
            sb.replace(index, index + 1, param);
            offset = index + param.length(); // 更新偏移量
        }
        return sb.toString();
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