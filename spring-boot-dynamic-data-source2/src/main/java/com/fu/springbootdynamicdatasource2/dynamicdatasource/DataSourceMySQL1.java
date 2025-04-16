package com.fu.springbootdynamicdatasource2.dynamicdatasource;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 数据源1
 */
@Configuration
@MapperScan(value = "com.fu.springbootdynamicdatasource2.mapper.mysql1", sqlSessionFactoryRef = "mysql1SqlSessionFactory")
public class DataSourceMySQL1 {
    @Value("${spring.datasource.mysql1.mapper-locations}")
    private String mapperLocations;

    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.mysql1")
    public DataSource mysql1DataSource() {
        return new HikariDataSource();
    }

    @Primary
    @Bean
    public DataSourceTransactionManager mysql1TransactionManager() {
        return new DataSourceTransactionManager(mysql1DataSource());
    }

    @Primary
    @Bean
    public SqlSessionFactory mysql1SqlSessionFactory(@Qualifier("mysql1DataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        //设置mapper.xml文件位置
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        return sessionFactory.getObject();
    }
}
