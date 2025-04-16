package com.fu.basedemo.designpattern.singleton.usecase;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 单例模式：数据库连接池
 */
public class DataSourceSingleton {

    private static volatile DataSource dataSource;

    // 私有化构造器防止实例化
    private DataSourceSingleton() {
    }

    public static DataSource getInstance() {
        if (dataSource == null) {
            synchronized (DataSourceSingleton.class) {
                if (dataSource == null) {
                    Properties properties = new Properties();

                    //getResourceAsStream()函数默认在src/main/resources目录下查找文件。
                    try (InputStream input = DataSourceSingleton.class.getClassLoader().getResourceAsStream("properties/database.properties")) {
                        if (input == null) {
                            throw new RuntimeException("Sorry, unable to find database.properties");
                        }
                        // 加载属性文件
                        properties.load(input);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    HikariConfig config = new HikariConfig();
                    config.setJdbcUrl(properties.getProperty("jdbc.url"));
                    config.setUsername(properties.getProperty("jdbc.username"));
                    config.setPassword(properties.getProperty("jdbc.password"));
                    config.setMaximumPoolSize(Integer.parseInt(properties.getProperty("jdbc.poolSize")));
                    config.setConnectionTimeout(Long.parseLong(properties.getProperty("jdbc.connectionTimeout")));
                    dataSource = new HikariDataSource(config);
                }
            }
        }
        return dataSource;
    }

    // 获取数据源
    public static Connection getConnection() throws SQLException {
        return DataSourceSingleton.getInstance().getConnection();
    }

}
