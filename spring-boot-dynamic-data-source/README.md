# JTA 默认配置

```yaml
spring:
  jta:
    # 全局 JTA 配置
    enabled: true                    # 是否启用 JTA，默认 true
    transaction-manager-id:          # 事务管理器 ID，用于日志标识（默认主机名 + 随机数）
    log-dir: ./transaction-logs      # 事务日志目录（覆盖 atomikos 的 log-base-dir）
    atomikos:

      # 全局事务管理器配置
      properties:
        allow-sub-transactions: true  # 是否允许子事务，默认 true
        checkpoint-interval: 500       # 检查点间隔（毫秒），默认 500
        default-jta-timeout: 10000    # 默认 JTA 超时时间（毫秒），默认 10000（10秒）
        default-max-wait-time-on-shutdown: 0x7fffffffffffffffL  # 关闭时最大等待时间（毫秒），默认 Long.MAX_VALUE
        enable-logging: true          # 是否启用事务日志，默认 true
        force-shutdown-on-vm-exit: false  # VM退出时强制关闭，默认 false
        log-base-dir: ./transaction-logs  # 事务日志目录，默认当前目录下的 transaction-logs
        log-base-name: tmlog          # 日志文件基础名称，默认 tmlog
        max-actives: 50              # 最大活动事务数，默认 50
        max-timeout: 300000          # 最大事务超时时间（毫秒），默认 300000（5分钟）
        recovery:
          delay: 10000               # 恢复延迟（毫秒），默认 10000（10秒）
          forget-orphaned-log-entries-delay: 86400000  # 清理孤儿日志延迟（毫秒）（默认24小时）
          max-retries: 5             # 最大重试次数，默认 5
          retry-interval: 10000      # 重试间隔（毫秒），默认 10000
        serial-jta-transactions: true  # 是否序列化 JTA 事务，默认 true
        service:                      # 事务服务实现类（默认自动选择）
        threaded-two-phase-commit: false  # 是否启用多线程两阶段提交（设置为true的话会提高性能但有风险），默认 false
        transaction-manager-unique-name: # 事务管理器唯一名称（默认自动生成）

      # 数据源连接池配置(管理 数据库连接池（如 MySQL、Oracle 等支持 XA 协议的数据库）)
      datasource:
        default-isolation-level:  # 数据库默认事务隔离级别（-1=数据库决定, 1=READ_UNCOMMITTED, 2=READ_COMMITTED, 3=REPEATABLE_READ, 4=SERIALIZABLE），默认由数据库决定，即：-1
        test-query:               # 连接验证 SQL（如 SELECT 1），默认无（生产环境建议调整）
        login-timeout: 0         # 连接超时时间（秒），0表示无限制，默认 0
        concurrent-connection-validation: true  # 是否启用并发连接验证，默认 true
        xa-data-source-class-name: # XA 数据源的全限定类名（必填，如 com.mysql.cj.jdbc.MysqlXADataSource）
        xa-properties: # XA 数据源自定义属性（必填，如 url、user、password等）
          自定义1: value
        # 下面的配置是和 connectionfactory 相同的配置
        max-pool-size: 1          # 最大连接池大小，默认 1（生产环境建议调整）
        min-pool-size: 1          # 最小连接池大小，默认 1（生产环境建议调整）
        maintenance-interval: 60 # 连接池维护间隔（秒），默认 60
        max-idle-time: 60         # 连接最大空闲时间（秒），默认 60
        reap-timeout: 0          # 回收超时时间（秒），默认 0（不启用）（生产环境建议调整）
        borrow-connection-timeout: 30  # 从连接池获取连接的超时时间（秒），默认 30
        max-lifetime: 0           # 连接最大存活时间（秒），0 表示不限制，默认 0
        unique-resource-name:     # 唯一资源名称（自动生成，默认基于数据源配置）

      # JMS/XA 连接工厂配置(管理 JMS/XA 连接工厂（如 ActiveMQ、RabbitMQ 等支持 XA 的消息中间件）)
      connectionfactory:
        ignore-session-transacted-flag: true  # 是否忽略会话事务标志，默认 true
        local-transaction-mode: false  # 是否启用本地事务模式，默认 false
        xa-connection-factory-class-name: org.apache.activemq.ActiveMQXAConnectionFactory # XA 连接工厂类名（如 ActiveMQXAConnectionFactory）
        xa-properties: # XA 连接工厂自定义属性
          自定义2: value
        # 下面的配置是和 datasource 相同的配置
        max-pool-size: 1          # 最大连接池大小，默认 1（生产环境建议调整）
        min-pool-size: 1          # 最小连接池大小，默认 1（生产环境建议调整）
        maintenance-interval: 60 # 连接池维护间隔（秒），默认 60
        max-idle-time: 60         # 连接最大空闲时间（秒），默认 60
        reap-timeout: 0          # 回收超时时间（秒），默认 0（不启用）（生产环境建议调整）
        borrow-connection-timeout: 30  # 从连接池获取连接的超时时间（秒），默认 30
        max-lifetime: 0           # 连接最大存活时间（秒），0 表示不限制，默认 0
        unique-resource-name:     # 唯一资源名称（自动生成，默认基于数据源配置）
```

```text
建议： 生产环境需重点关注连接池配置、事务超时设置和日志存储位置，分布式事务场景建议配置合理的 max-retries 和 recovery.delay。
```

## 增加新数据源注意事项

1. DataSourceConfiguration.BASE_PACKAGES 需要注意是否是项目的根路径
2. DataSourceConfiguration.DATASOURCES 在配置文件是否使用的是 datasources 作为根路径
3. mybatis mapper.xml 映射路径在配置文件里是否使用的是 datasources.mapper-locations 作为根路径（datasources 就是第2点的变量名）

### 实现 DataSourceConfiguration 接口注意事项

X表示当前类的名称，复制一个 DataSourceConfiguration 实现类，直接修改类名为新的数据源名称，
不要使用 IDEA 的 Rename 功能！这样你改了名字只会就知道要修改哪些地方了。

1. @MapperScan 注解里的 basePackages 变量值中的 X.dataSourceName 是否是当前类名
2. @MapperScan 注解里的 sqlSessionFactoryRef 变量值中的 X.sqlSessionFactoryBeanName 是否是当前类名
3. 当前类的 public static final String dataSourceName 变量值是否是当前类名全小写字母
