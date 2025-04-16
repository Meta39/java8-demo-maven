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
 * 数据源2
 */
@Configuration
@MapperScan(value = "com.fu.springbootdynamicdatasource2.mapper.mysql2", sqlSessionFactoryRef = "mysql2SqlSessionFactory")
public class DataSourceMySQL2 {
    @Value("${spring.datasource.mysql2.mapper-locations}")
    private String mapperLocations;

    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.mysql2")
    public DataSource mysql2DataSource() {
        return new HikariDataSource();
    }

    @Bean
    @Primary
    public DataSourceTransactionManager mysql2TransactionManager() {
        return new DataSourceTransactionManager(mysql2DataSource());
    }

    @Bean
    @Primary
    public SqlSessionFactory mysql2SqlSessionFactory(@Qualifier("mysql2DataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        //设置mapper.xml文件位置
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        return sessionFactory.getObject();
    }
}
