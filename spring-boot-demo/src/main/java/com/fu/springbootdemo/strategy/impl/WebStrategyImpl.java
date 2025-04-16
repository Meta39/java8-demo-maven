package com.fu.springbootdemo.strategy.impl;

import com.fu.springbootdemo.strategy.Strategy;
import com.fu.springbootdemo.strategy.vo.WebTokenInfo;
import org.springframework.stereotype.Component;

/**
 * 3.实现web登录策略
 */
@Component("webLogin")
public class WebStrategyImpl implements Strategy<WebTokenInfo> {

    @Override
    public WebTokenInfo login(String username, String password) {
        //查询web端的数据库，判断用户名和密码是否正确。

        //正确则返回web端的信息给前端
        WebTokenInfo webTokenInfo = new WebTokenInfo();
        webTokenInfo.setWebToken("web端的token");
        webTokenInfo.setWebUserId(1);
        return webTokenInfo;
    }
}
