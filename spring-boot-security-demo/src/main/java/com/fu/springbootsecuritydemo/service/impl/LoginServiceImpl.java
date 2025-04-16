package com.fu.springbootsecuritydemo.service.impl;

import com.fu.springbootsecuritydemo.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.*;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public String login(String name, String password) {
        log.info("密码加密后的值：{}",bCryptPasswordEncoder.encode(password));

        /**
         * 因为我使用了全局异常处理，GobalExceptionHandler会自动捕获controller层抛出的异常
         * authenticationManager.authenticate 这一句认证失败会抛出AuthenticationException异常
         * 我定义了认证失败处理器无法获取到 AuthenticationException 异常，因为全局异常处理已经捕获了
         * 然后 AuthenticationException 异常不属于 ServicesException，所以会返回500，服务器响应错误
         */
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(name, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if (Objects.isNull(authenticate)) {
            //用户名密码错误
            throw new RuntimeException("用户名密码错误");
        }
        User securityUser = (User) authenticate.getPrincipal();
        String token = UUID.randomUUID().toString();

        //把token和用户信息存到redis中，并设置过期时间。把这个时间也返回给前端，让前端定时检查是否超过这个时间，如果超过这个时间就调续期接口，对token进行续期。
        redisTemplate.opsForValue().set(token, securityUser, Duration.ofSeconds(3600L));
        //将用户存入上下文中
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return token;
    }

    @Override
    public void logout(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User securityUser = (User) authentication.getPrincipal();
        String username = securityUser.getUsername();
        String token = request.getHeader("token");
        if (Boolean.TRUE.equals(redisTemplate.hasKey(token))){
            redisTemplate.delete(token);
        }
        //清除上下文
        SecurityContextHolder.clearContext();
    }

}

