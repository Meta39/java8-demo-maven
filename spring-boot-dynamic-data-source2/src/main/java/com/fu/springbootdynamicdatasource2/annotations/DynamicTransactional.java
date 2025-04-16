package com.fu.springbootdynamicdatasource2.annotations;

import java.lang.annotation.*;

/**
 * 多数据源事务注解
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DynamicTransactional {

    /**
     * 事务管理器数组
     */
    String[] transactionManagers() default {"mysql1TransactionManager", "mysql2TransactionManager"};

}
