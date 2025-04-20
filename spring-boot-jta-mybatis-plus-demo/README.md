# MyBatis和MyBatis-Plus使用JTA的区别
1. MyBatis-Plus 自定义配置使用的是自己的 MybatisSqlSessionFactoryBean，而不是 SqlSessionFactoryBean
2. MyBatis-Plus 需要在实体类增加@TableName、@TableId注解
3. MyBatis-Plus 插件需要配置自己的插件 @Bean MybatisPlusInterceptor 
