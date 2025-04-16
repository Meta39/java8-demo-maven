package com.fu.springbootdemo.global;

import lombok.Data;

@Data
public class UpdatePwdDTO {
    private String pwd;//旧密码
    private String newPwd;//新密码
}
