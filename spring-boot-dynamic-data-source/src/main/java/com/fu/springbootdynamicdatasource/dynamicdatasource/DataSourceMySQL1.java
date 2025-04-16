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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * 数据源1
 */
@Configuration
@MapperScan(value = "com.fu.springbootdynamicdatasource.mapper.mysql1", sqlSessionFactoryRef = "mysql1SqlSessionFactory")
public class DataSourceMySQL1 {
    @Value("${spring.datasource.mysql1.mapper-locations}")
    private String mapperLocations;

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.mysql1")
    public MysqlXADataSource mysql1XADatasource() {
        return DataSourceBuilder.create().type(MysqlXADataSource.class).build();
    }

    @Bean
    @Primary
    public DataSource mysql1Datasource(@Qualifier("mysql1XADatasource") MysqlXADataSource dataSource) {
        // 将本地事务注册到创 Atomikos全局事务
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(dataSource);
        xaDataSource.setUniqueResourceName("mySQL1DataSource");
        return xaDataSource;
    }

    @Bean
    @Primary
    public SqlSessionFactory mysql1SqlSessionFactory(@Qualifier("mysql1Datasource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        return bean.getObject();
    }

    @Bean
    @Primary
    public SqlSessionTemplate mysql1SqlSessionTemplate(@Qualifier("mysql1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
