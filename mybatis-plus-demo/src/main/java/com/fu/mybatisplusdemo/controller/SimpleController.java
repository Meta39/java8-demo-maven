package com.fu.mybatisplusdemo.controller;

import com.fu.mybatisplusdemo.entity.SimpleObject;
import com.fu.mybatisplusdemo.mapper.SimpleObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("simple")
@RequiredArgsConstructor
public class SimpleController {
    private final SimpleObjectMapper simpleObjectMapper;
    private final RestTemplate restTemplate;
    private final PlatformTransactionManager platformTransactionManager;//事务管理器
    private final TransactionDefinition transactionDefinition;// 事务的一些基础信息，如超时时间、隔离级别、传播属性等


    /**
     * 开启事务过程中，如果远程调用查询当前已经开启但没有提交的事务，就会查不到数据。（错误示例）
     */
    @GetMapping("insert")
    @Transactional(rollbackFor = Exception.class)//1.开启事务
    public void insert() {
        SimpleObject simpleObject = new SimpleObject();
        simpleObject.setId(2);
        simpleObject.setName("name" + System.currentTimeMillis());
        simpleObjectMapper.insert(simpleObject);//2.因为开启了事务，所以这里的 insert 并没有 commit，导致下面远程调用查询数据是空的。
        SimpleObject simpleObject1 = restTemplate.getForEntity("http://localhost:8080/simple/2", SimpleObject.class, simpleObject.getId()).getBody();
        log.info("simpleObject1 （这里会输出null）：{}",simpleObject1);//3.这里会输出null，因为事务没有提交，数据库不会新增数据
        if (Objects.isNull(simpleObject1)) {
            throw new RuntimeException("simpleObject1 is null");
        }
    }

    /**
     * 使用手动事务，远程调用可以查询到数据。（推荐）
     */
    @GetMapping("insert2")
    public void insert2() {
        //手动事务不能加@Transactional注解，否则优先使用@Transactional注解的事务
        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);//1、手动获取事务
        SimpleObject simpleObject = new SimpleObject();
        try {
            simpleObject.setId(2);
            simpleObject.setName("name" + System.currentTimeMillis());
            simpleObjectMapper.insert(simpleObject);
            platformTransactionManager.commit(transaction);//2.手动提交事务
        } catch (Exception e) {
            platformTransactionManager.rollback(transaction);//4.发生异常手动回滚事务
            log.error("insert2异常：", e);
            throw new RuntimeException( "insert2异常：" + e.getMessage());
        }
        SimpleObject simpleObject1 = restTemplate.getForEntity("http://localhost:8080/simple/2", SimpleObject.class, simpleObject.getId()).getBody();
        log.info("simpleObject1 （这里会输出simpleObject1）：{}",simpleObject1);//3.这里会输出id = 2的 SimpleObject 对象，因为事务提交，数据库会新增数据
        if (Objects.isNull(simpleObject1)) {
            throw new RuntimeException("simpleObject1 is null");
        }
    }

    /**
     * 不使用事务，远程调用可以查询到数据。（不推荐）
     */
    @GetMapping("insert3")
    public void insert3() {
        SimpleObject simpleObject = new SimpleObject();
        simpleObject.setId(2);
        simpleObject.setName("name" + System.currentTimeMillis());
        simpleObjectMapper.insert(simpleObject);//1、因为没有事务，所以这里会新增数据到数据库
        SimpleObject simpleObject1 = restTemplate.getForEntity("http://localhost:8080/simple/2", SimpleObject.class, simpleObject.getId()).getBody();
        log.info("simpleObject1 （这里会输出simpleObject1）：{}",simpleObject1);//2.这里会输出id = 2的 SimpleObject 对象，因为事务提交，数据库会新增数据
        if (Objects.isNull(simpleObject1)) {
            throw new RuntimeException("simpleObject1 is null");
        }
    }

    /**
     * 被远程调用的查询方法
     */
    @GetMapping("{id}")
    public SimpleObject selectById(@PathVariable Integer id) {
        return simpleObjectMapper.selectById(id);
    }

}
