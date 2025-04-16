package com.fu.springbootdemo.strategy.vo;

import lombok.Data;

/**
 * web端token存储的信息
 */
@Data
public class WebTokenInfo {
    private String webToken; //web端的token
    private Integer webUserId; //web端的用户id
}
