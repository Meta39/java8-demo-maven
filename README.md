# java8-demo-maven

```text
用java8、maven学习的demo
```

## 运行环境要求

1. java8(java1.8)
2. maven3+

## JTA与JDBC事务的区别

1. JDBC事务：仅针对单个数据库连接，通过Connection.commit()/rollback()控制。
2. JTA事务：可跨多个资源，通过全局事务管理器协调。

### JTA的核心组成

```text
JTA（Java Transaction API）是Java平台提供的一套用于管理分布式事务的API，属于Java EE（现Jakarta EE）规范的一部分。
它允许应用程序以统一的方式协调跨多个资源（如数据库、消息队列等）的事务，确保数据的ACID（原子性、一致性、隔离性、持久性）特性。
```

1. 提供编程式事务管理接口（如UserTransaction），开发者可以通过代码显式控制事务的边界（开始、提交、回滚）。

#### 高层API

```java
UserTransaction utx = getUserTransaction();
utx.begin();
try {
        // 执行跨资源的操作（如更新数据库、发送消息）
        utx.commit();
} catch (Exception e) {
        utx.rollback();
}
```

#### 低层API

```text
基于XA协议，通过XAResource接口与资源管理器（如数据库、消息中间件）交互，实现两阶段提交（2PC）。
资源需支持XA协议（如MySQL的XA驱动、ActiveMQ的XA连接）。
```

### JTA优缺点

#### 优点：

1. 提供分布式事务的标准解决方案。
2. 支持多资源的一致性。

#### 缺点：

1. 性能开销较大（两阶段提交的阻塞问题）。
2. 配置复杂，需依赖支持XA的资源。

### 现代替代方案

#### 在微服务架构中，JTA的XA模式可能因性能问题不适用，常采用以下替代方案：

1. Saga模式：通过补偿事务实现最终一致性。
2. 本地消息表：异步确保事务完成。
3. Seata等框架：提供AT模式（自动补偿型事务）。
