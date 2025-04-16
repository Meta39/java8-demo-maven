package com.fu.springbootdynamicdatasource.dynamicdatasource;

import com.mysql.cj.jdbc.MysqlXADataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * 数据源2
 */
@Configuration
@MapperScan(value = "com.fu.springbootdynamicdatasource.mapper.mysql2", sqlSessionFactoryRef = "mysql2SqlSessionFactory")
public class DataSourceMySQL2 {
    @Value("${spring.datasource.mysql2.mapper-locations}")
    private String mapperLocations;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.mysql2")
    public MysqlXADataSource mysql2XADataSource() {
        return DataSourceBuilder.create().type(MysqlXADataSource.class).build();
    }

    @Bean
    public DataSource mysql2XADataSource(@Qualifier("mysql2XADataSource") MysqlXADataSource dataSource) {
        // 将本地事务注册到创 Atomikos全局事务
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(dataSource);
        xaDataSource.setUniqueResourceName("mySQL2DataSource");
        return xaDataSource;
    }

    @Bean
    public SqlSessionFactory mysql2SqlSessionFactory(@Qualifier("mysql2XADataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate mysql2SqlSessionTemplate(@Qualifier("mysql2SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
