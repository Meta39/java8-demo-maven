package com.fu.springbootdemo.service;


import com.fu.springbootdemo.global.LoginDTO;
import com.fu.springbootdemo.global.TokenInfo;
import com.fu.springbootdemo.global.UpdatePwdDTO;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {

    /**
     * 登录接口
     */
    TokenInfo login(LoginDTO loginDTO);

    /**
     * 登出接口
     */
    Boolean logout(HttpServletRequest request);

    Boolean updatePwd(UpdatePwdDTO updatePwdDTO);

    /**
     * 续期token
     */
    Boolean token(HttpServletRequest request);

}
