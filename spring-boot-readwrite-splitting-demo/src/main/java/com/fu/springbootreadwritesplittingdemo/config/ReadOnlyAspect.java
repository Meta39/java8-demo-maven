package com.fu.springbootreadwritesplittingdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 创建日期：2024-05-28
 */
@Slf4j
@Aspect
@Component
public class ReadOnlyAspect {
    protected static final AtomicInteger DB_INDEX = new AtomicInteger(0);

    //事务切点
    @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void transactionalPointCut() {
    }

    //执行事务切点之前将信息存储到ThreadLocal
    @Before("transactionalPointCut()")
    public void beforeTransactionalMethod() {
        TransactionalThreadLocal.set(true);
    }

    @After("transactionalPointCut()")
    public void afterTransactionalMethod() {
        TransactionalThreadLocal.remove();
    }

    //===================================== 事务切点结束 =========================================

    //只读切点
    @Pointcut("@annotation(com.fu.springbootreadwritesplittingdemo.config.ReadOnly)")
    public void readOnlyPointCut() {
    }

    //执行只读切点前判断是否使用了事务，如果使用了事务，则不进行数据源切换
    @Before("readOnlyPointCut()")
    public void beforeReadOnlyMethod() {
        if (TransactionalThreadLocal.get()) {
            return;
        }
        SlaveDB nowDb = roundRobinDB();
        SlaveDBThreadLocal.set(nowDb);
        log.info("当前只读数据源为：{}", nowDb);
    }

    @After("readOnlyPointCut()")
    public void afterReadOnlyMethod() {
        SlaveDBThreadLocal.remove();
    }

    //===================================== 只读切点结束 =========================================

    @Before("transactionalPointCut() && readOnlyPointCut()")
    public void beforeTransactionalReadMethod() {
        throw new IllegalArgumentException("@Transactional 和 @ReadOnly 注解互斥，不能同时作用在同一个方法上");
    }


    /**
     * 轮询
     */
    protected SlaveDB roundRobinDB() {
        int index = DB_INDEX.get();
        if (index >= SlaveDB.VALUES_LENGTH - 1) {
            DB_INDEX.set(0);
            return SlaveDB.getByIndex(0);
        }
        ++index;
        DB_INDEX.set(index);
        return SlaveDB.getByIndex(index);
    }

}

