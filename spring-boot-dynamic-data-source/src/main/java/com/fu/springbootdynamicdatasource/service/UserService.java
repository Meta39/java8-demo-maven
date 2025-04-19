package com.fu.springbootdynamicdatasource.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fu.springbootdynamicdatasource.dynamicdatasource.impl.MySQL1;
import com.fu.springbootdynamicdatasource.dynamicdatasource.impl.MySQL2;
import com.fu.springbootdynamicdatasource.entity.User;
import com.fu.springbootdynamicdatasource.mapper.mysql1.MySQL1UserMapper;
import com.fu.springbootdynamicdatasource.mapper.mysql2.MySQL2UserMapper;
import com.fu.springbootdynamicdatasource.util.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.sql.XADataSource;
import java.util.Properties;

/**
 * 创建日期：2024-05-10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final ObjectMapper objectMapper;
    private final MySQL1UserMapper mySQL1UserMapper;
    private final MySQL2UserMapper mySQL2UserMapper;

    public void select() {
        //查看 MySQL1 是否有效
        MySQL1 mySQL1 = BeanUtils.getBean(MySQL1.class);
        AtomikosDataSourceBean dataSource1 = (AtomikosDataSourceBean) mySQL1.dataSource();
        if (!ObjectUtils.isEmpty(dataSource1)) {
            Properties xaProperties = dataSource1.getXaProperties();
            String xaDataSourceClassName = dataSource1.getXaDataSourceClassName();
            XADataSource xaDataSource = dataSource1.getXaDataSource();
            int minPoolSize = dataSource1.getMinPoolSize();
            int maxPoolSize = dataSource1.getMaxPoolSize();
            int borrowConnectionTimeout = dataSource1.getBorrowConnectionTimeout();
            int reapTimeout = dataSource1.getReapTimeout();
            int maintenanceInterval = dataSource1.getMaintenanceInterval();
            int maxIdleTime = dataSource1.getMaxIdleTime();
            int maxLifetime = dataSource1.getMaxLifetime();
            String testQuery = dataSource1.getTestQuery();
            log.info(
                    "MySQL1:xaProperties{},xaDataSourceClassName:{},xaDataSource:{},minPoolSize:{},maxPoolSize:{},borrowConnectionTimeout:{},reapTimeout:{},maintenanceInterval:{},maxIdleTime:{},maxLifetime:{},testQuery:{}",
                    xaProperties,
                    xaDataSourceClassName,
                    xaDataSource,
                    minPoolSize,
                    maxPoolSize,
                    borrowConnectionTimeout,
                    reapTimeout,
                    maintenanceInterval,
                    maxIdleTime,
                    maxLifetime,
                    testQuery
            );
        }

        //查看 MySQL2 是否有效
        MySQL2 mySQL2 = BeanUtils.getBean(MySQL2.class);
        AtomikosDataSourceBean dataSource2 = (AtomikosDataSourceBean) mySQL2.dataSource();
        if (!ObjectUtils.isEmpty(dataSource2)) {
            Properties xaProperties = dataSource2.getXaProperties();
            String xaDataSourceClassName = dataSource2.getXaDataSourceClassName();
            XADataSource xaDataSource = dataSource2.getXaDataSource();
            int minPoolSize = dataSource2.getMinPoolSize();
            int maxPoolSize = dataSource2.getMaxPoolSize();
            int borrowConnectionTimeout = dataSource2.getBorrowConnectionTimeout();
            int reapTimeout = dataSource2.getReapTimeout();
            int maintenanceInterval = dataSource2.getMaintenanceInterval();
            int maxIdleTime = dataSource2.getMaxIdleTime();
            int maxLifetime = dataSource2.getMaxLifetime();
            String testQuery = dataSource2.getTestQuery();
            log.info(
                    "MySQL1:xaProperties{},xaDataSourceClassName:{},xaDataSource:{},minPoolSize:{},maxPoolSize:{},borrowConnectionTimeout:{},reapTimeout:{},maintenanceInterval:{},maxIdleTime:{},maxLifetime:{},testQuery:{}",
                    xaProperties,
                    xaDataSourceClassName,
                    xaDataSource,
                    minPoolSize,
                    maxPoolSize,
                    borrowConnectionTimeout,
                    reapTimeout,
                    maintenanceInterval,
                    maxIdleTime,
                    maxLifetime,
                    testQuery
            );
        }

        User user = mySQL1UserMapper.selectById(1);
        User user2 = mySQL2UserMapper.selectById(1);
        log.info("user:{}", user);
        log.info("user2:{}", user2);
    }

    @Transactional//使用的是JTA事务
    public void insertUser() {
        mySQL2UserMapper.insert(new User(2, "哈哈"));
        mySQL1UserMapper.insert(new User(2, "哈哈"));
        int i = 1 / 0;
    }

}
