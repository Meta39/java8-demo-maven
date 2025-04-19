package com.fu.springbootdynamicdatasource.dynamicdatasource.impl;

import com.fu.springbootdynamicdatasource.dynamicdatasource.DataSourceConfiguration;
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

import javax.sql.DataSource;

/**
 * 数据源1
 * 只需要配置数据源名称即可。
 * 复制一个 DataSourceConfiguration 实现类，直接修改类名为新的数据源名称，
 * 不要使用 IDEA 的 Rename 功能！这样你改了名字只会就知道要修改哪些地方了。
 * 1.@MapperScan 注解里的 basePackages 变量值中的 X.dataSourceName 是否是当前类名
 * 2.@MapperScan 注解里的 sqlSessionFactoryRef 变量值中的 X.sqlSessionFactoryBeanName 是否是当前类名
 * 3.当前类的 public static final String dataSourceName 变量值是否是当前类名全小写字母
 */
@Configuration
@MapperScan(basePackages = DataSourceConfiguration.BASE_PACKAGES + DataSourceConfiguration.DOT + MySQL1.dataSourceName,
        sqlSessionFactoryRef = MySQL1.sqlSessionFactoryBeanName,
        sqlSessionTemplateRef = MySQL1.sqlSessionTemplateBeanName
)
public class MySQL1 implements DataSourceConfiguration {
    //需要看看上面basePackages、sqlSessionFactoryRef、sqlSessionTemplateRef是否当前类的变量名
    public static final String dataSourceName = "mysql1";
    public static final String sqlSessionTemplateBeanName = SQL_SESSION_TEMPLATE + dataSourceName;
    public static final String sqlSessionFactoryBeanName = SQL_SESSION_FACTORY + dataSourceName;
    //不需要关注
    private static final String dataSourceBeanName = DATA_SOURCE + dataSourceName;
    private static final String dataSourceProperties = DATASOURCES + DOT + dataSourceName;
    @Value("${" + dataSourceProperties + DOT + MAPPER_LOCATIONS_PROPERTIES + "}")
    private String mapperLocations;

    /**
     * 配置XA数据源
     */
    @Primary
    @Bean(dataSourceBeanName)
    //使用yml文件里面的配置，yml文件里面的配置变量名不要改其它名字，因为用的都是JTA设置的变量名
    @ConfigurationProperties(prefix = dataSourceProperties)
    @Override
    public DataSource dataSource() {
        //这里会自动使用配置文件里面配置的值，注意 xa-data-source-class-name 一定要配置XA的数据库驱动
        return new AtomikosDataSourceBean();
    }

    @Primary
    @Bean(sqlSessionFactoryBeanName)
    @Override
    public SqlSessionFactory sqlSessionFactory(@Qualifier(dataSourceBeanName) DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(getResources(mapperLocations));
        return bean.getObject();
    }

    @Primary
    @Bean(sqlSessionTemplateBeanName)
    @Override
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier(sqlSessionFactoryBeanName) SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
