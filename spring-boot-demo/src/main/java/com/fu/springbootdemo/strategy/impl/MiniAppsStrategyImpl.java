package com.fu.springbootdemo.strategy.impl;

import com.fu.springbootdemo.strategy.Strategy;
import com.fu.springbootdemo.strategy.vo.MiniAppsTokenInfo;
import org.springframework.stereotype.Component;

/**
 * 3.实现小程序登录策略
 */
@Component("miniAppsLogin")
public class MiniAppsStrategyImpl implements Strategy<MiniAppsTokenInfo> {

    @Override
    public MiniAppsTokenInfo login(String username, String password) {
        //查询web端的数据库，判断用户名和密码是否正确。

        //正确则返回web端的信息给前端
        MiniAppsTokenInfo miniAppsTokenInfo = new MiniAppsTokenInfo();
        miniAppsTokenInfo.setMiniAppsToken("小程序端的token");
        miniAppsTokenInfo.setMiniAppsUserId(2);
        return miniAppsTokenInfo;
    }
}
