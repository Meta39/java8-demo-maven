package com.fu.springbootjta.datasources;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * 定义数据源需要实现的接口，防止漏配置
 */
public interface DataSourceConfiguration {
    /**
     * 重点
     * mapper 接口文件夹路径根节点，mapper 文件夹下存放的是各自数据源实现类 dataSourceName 变量名的文件夹。
     */
    String BASE_PACKAGES = "com.fu.springbootjta.mapper";
    /**
     * 重点
     * 配置文件顶级节点名称
     */
    String DATASOURCES = "datasources";

    /**
     * 重点
     * 配置文件 mapper 资源存放路径变量名
     */
    String MAPPER_LOCATIONS_PROPERTIES = "mapper-locations";

    //-------- 下面的内容无需关注 --------

    /**
     * 点
     */
    String DOT = ".";

    /**
     * 无需关注
     * 数据源 bean name 前缀
     */
    String DATA_SOURCE = "dataSource";

    /**
     * 无需关注
     * SQL会话模板 bean name 前缀
     */
    String SQL_SESSION_TEMPLATE = "sqlSessionTemplate";

    /**
     * 无需关注
     * SQL会话工厂 bean name 前缀
     */
    String SQL_SESSION_FACTORY = "sqlSessionFactory";

    /**
     * XA数据源
     */
    DataSource dataSource();

    /**
     * SQL会话工厂
     *
     * @param dataSource 上面配置的XA数据源
     */
    SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception;

    /**
     * SQL会话模板
     *
     * @param sqlSessionFactory 上面配置的SQL会话工厂
     */
    SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory);

    /**
     * 获取 mapper 资源
     *
     * @param mapperLocations mapper.xml文件路径
     */
    default Resource[] getResources(String mapperLocations) throws IOException {
        return new PathMatchingResourcePatternResolver().getResources(mapperLocations);
    }

}
