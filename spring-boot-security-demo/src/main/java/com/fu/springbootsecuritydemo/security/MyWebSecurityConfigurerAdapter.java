package com.fu.springbootsecuritydemo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MyWebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private MyOncePerRequestFilter myOncePerRequestFilter;

    @Autowired
    private MyAuthenticationEntryPoint myAuthenticationEntryPoint;

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;
    @Autowired
    private MyPermissionEvaluator myPermissionEvaluator;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler(){
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler =new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setPermissionEvaluator(myPermissionEvaluator);
        return defaultWebSecurityExpressionHandler;
    }

    /**
     * 配置密码加密单例模式，后续加/解密只需要注入BCryptPasswordEncoder直接调用即可。
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
        //1、关闭csrf，关闭Session
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //2、设置不需要认证的URL
        http.authorizeRequests()
                .expressionHandler(defaultWebSecurityExpressionHandler())
                .antMatchers("/login").anonymous()//允许未登录的用户进行访问
                .anyRequest().authenticated();//其余url都要认证才能访问
        //3、在UsernamePasswordAuthenticationFilter前添加认证过滤器
        http.addFilterBefore(myOncePerRequestFilter, UsernamePasswordAuthenticationFilter.class);
        //4、异常处理
        http.exceptionHandling()
                .authenticationEntryPoint(myAuthenticationEntryPoint)//认证失败处理器
                .accessDeniedHandler(myAccessDeniedHandler);//权限不足处理器
        //5、允许跨域
        http.cors();
        return http.build();
    }

}

