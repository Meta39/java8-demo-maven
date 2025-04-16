1.在任何情况下，使用 spring boot 时都不应该自己实现单例（Singleton），
因为它是每个用 @Component（或其子结构 @Service、@Repository 和 @Controller）注释的类的标准。

2.不要在 spring boot 中自己实现工厂模式，因为大多数情况下，我们直接依赖注入即可。