package com.fu.springbootdynamicdatasource.dynamicdatasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
@MapperScan(basePackages = "com.fu.springbootdynamicdatasource.mapper.mysql1",
        sqlSessionFactoryRef = DataSourceMySQL1Configuration.sqlSessionFactoryMySQL1,
        sqlSessionTemplateRef = DataSourceMySQL1Configuration.sqlSessionTemplateMySQL1
)
public class DataSourceMySQL1Configuration {
    public static final String dataSourceMySQL1 = "dataSourceMySQL1";
    public static final String sqlSessionTemplateMySQL1 = "sqlSessionTemplateMySQL1";
    public static final String sqlSessionFactoryMySQL1 = "sqlSessionFactoryMySQL1";
    @Value("${datasources.mysql1.mapper-locations}")
    private String mapperLocations;

    @Primary
    @Bean(dataSourceMySQL1)
    @ConfigurationProperties(prefix = "datasources.mysql1")
    public DataSource dataSourceMySQL1() {
        return new AtomikosDataSourceBean();
    }

    @Primary
    @Bean(sqlSessionFactoryMySQL1)
    public SqlSessionFactory sqlSessionFactoryMySQL1(@Qualifier(dataSourceMySQL1) DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        return bean.getObject();
    }

    @Primary
    @Bean(sqlSessionTemplateMySQL1)
    public SqlSessionTemplate sqlSessionTemplateMySQL1(@Qualifier(sqlSessionFactoryMySQL1) SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
