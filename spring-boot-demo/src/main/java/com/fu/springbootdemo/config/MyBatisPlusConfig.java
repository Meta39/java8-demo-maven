package com.fu.springbootdemo.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fu.springbootdemo.util.DateTimeUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * MyBatisPlus分页、乐观锁插件
 */
@Configuration
public class MyBatisPlusConfig implements MetaObjectHandler{

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        //添加分页插件。addInnerInterceptor设置数据库类型
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        //添加乐观锁插件， @Version//标识乐观锁版本号字段。（个人感觉一般般不是很好用）
        //mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {

            // 反序列化（接收数据）
            builder.deserializerByType(LocalDateTime.class,
                    new LocalDateTimeDeserializer(DateTimeUtils.getFormatter(DateTimeUtils.DEFAULT_DATE_TIME_FORMAT)));
            builder.deserializerByType(LocalDate.class,
                    new LocalDateDeserializer(DateTimeUtils.getFormatter(DateTimeUtils.DEFAULT_DATE_FORMAT)));
            builder.deserializerByType(LocalTime.class,
                    new LocalTimeDeserializer(DateTimeUtils.getFormatter(DateTimeUtils.DEFAULT_TIME_FORMAT)));

            // 序列化（返回数据）
            builder.serializerByType(LocalDateTime.class,
                    new LocalDateTimeSerializer(DateTimeUtils.getFormatter(DateTimeUtils.DEFAULT_DATE_TIME_FORMAT)));
            builder.serializerByType(LocalDate.class,
                    new LocalDateSerializer(DateTimeUtils.getFormatter(DateTimeUtils.DEFAULT_DATE_FORMAT)));
            builder.serializerByType(LocalTime.class,
                    new LocalTimeSerializer(DateTimeUtils.getFormatter(DateTimeUtils.DEFAULT_TIME_FORMAT)));
        };
    }
}

