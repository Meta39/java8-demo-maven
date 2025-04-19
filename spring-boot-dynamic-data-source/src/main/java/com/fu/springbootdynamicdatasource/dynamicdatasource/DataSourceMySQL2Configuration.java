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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * 数据源2
 */
@Configuration
@MapperScan(value = "com.fu.springbootdynamicdatasource.mapper.mysql2",
        sqlSessionFactoryRef = DataSourceMySQL2Configuration.sqlSessionFactoryMySQL2,
        sqlSessionTemplateRef = DataSourceMySQL2Configuration.sqlSessionTemplateMySQL2
)
public class DataSourceMySQL2Configuration {
    public static final String dataSourceMySQL2 = "dataSourceMySQL2";
    public static final String sqlSessionFactoryMySQL2 = "sqlSessionFactoryMySQL2";
    public static final String sqlSessionTemplateMySQL2 = "sqlSessionTemplateMySQL2";
    @Value("${datasources.mysql2.mapper-locations}")
    private String mapperLocations;

    @Bean(dataSourceMySQL2)
    @ConfigurationProperties(prefix = "datasources.mysql2")
    public DataSource mysql2XADataSource() {
        return new AtomikosDataSourceBean();
    }

    @Bean(sqlSessionFactoryMySQL2)
    public SqlSessionFactory mysql2SqlSessionFactory(@Qualifier(dataSourceMySQL2) DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        return bean.getObject();
    }

    @Bean(sqlSessionTemplateMySQL2)
    public SqlSessionTemplate mysql2SqlSessionTemplate(@Qualifier(sqlSessionFactoryMySQL2) SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
