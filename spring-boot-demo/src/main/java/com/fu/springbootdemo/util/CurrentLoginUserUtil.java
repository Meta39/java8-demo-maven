package com.fu.springbootdemo.util;

import com.fu.springbootdemo.exception.UnauthorizedException;
import com.fu.springbootdemo.global.GlobalVariable;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 当前登录用户工具类
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public abstract class CurrentLoginUserUtil {

    /**
     * 根据Token获取当前登录用户的ID。
     * 警告：该方法必须经过认证才能使用！即调用该方法的请求头必须包含token
     */
    public static int getUserId(){
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        //Redis的Token key 就是UUID + 用户ID，删除32位长度的UUID以后就是用户ID。
        String redisTokenKey = request.getHeader(GlobalVariable.TOKEN);
        if (!StringUtils.hasText(redisTokenKey)){
            throw new UnauthorizedException();
        }
        return Integer.parseInt(redisTokenKey.substring(32));
    }

}
