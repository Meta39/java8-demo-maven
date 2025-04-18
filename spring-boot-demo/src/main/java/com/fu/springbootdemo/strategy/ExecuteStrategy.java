package com.fu.springbootdemo.strategy;

import com.fu.springbootdemo.util.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * 4.根据前端传的bean的名字去执行具体的策略
 */
@Component
public class ExecuteStrategy {

    public Object executeStrategy(String strategy, String username, String password) {
        return ((Strategy<?>) BeanUtils.getBean(strategy)).login(username, password);
    }
}
