package com.fu.springbootdemo.strategy.controller;

import com.fu.springbootdemo.strategy.ExecuteStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 5.策略模式登录接口，实现多端登录
 */
@RestController
@RequestMapping("strategy")
@RequiredArgsConstructor
public class StrategyController {
    private final ExecuteStrategy executeStrategy;

    /**
     * 登录接口
     * @param strategy 策略名称，即：XXXStrategyImpl的@Component("name")的name。
     * @param username 用户名（这里是为了策略而策略，所以假设用户名不同端存在不同的数据库中）
     * @param password 用户密码（这里是为了策略而策略，所以假设密码不同端存在不同的数据库中）
     */
    @GetMapping("{strategy}")
    public Object login(@PathVariable String strategy, @RequestParam("username") String username,@RequestParam("password") String password){
        //根据前端传递的strategy策略参数，通过Spring的Bean的name去执行具体的策略。
        return this.executeStrategy.executeStrategy(strategy,username,password);
    }
}
