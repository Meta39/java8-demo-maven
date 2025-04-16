package com.fu.springbootdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 只配置静态资源拦截，不要在这配置跨域访问，因为配置了过滤器，这里的跨域访问就会失效。
 * 应该在implements Filter的过滤器里面配置跨域访问
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 静态资源拦截
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                //classpath:后面不要加/否则路径就会错误，防止错误路径的办法就是先打印路径，路径正确在加进来。
                //取消模板static路径拦截
//                .addResourceLocations("classpath:META-INF/static")
                //取消转换文件拦截
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/");
    }
}
