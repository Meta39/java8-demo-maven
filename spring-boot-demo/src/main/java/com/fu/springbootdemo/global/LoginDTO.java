package com.fu.springbootdemo.global;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginDTO {
    @NotEmpty(message = "用户名不能为空")
    private String username; //用户名
    @NotEmpty(message = "密码不能为空")
    @Length(min = 172,max = 172,message = "加密密码长度应为172个字符串")
    private String pwd; //密码
}
