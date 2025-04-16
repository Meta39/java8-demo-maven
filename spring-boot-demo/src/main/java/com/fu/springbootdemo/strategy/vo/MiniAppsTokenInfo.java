package com.fu.springbootdemo.strategy.vo;

import lombok.Data;

/**
 * web端token存储的信息
 */
@Data
public class MiniAppsTokenInfo {
    private String miniAppsToken; //小程序端的token
    private Integer miniAppsUserId; //小程序端的用户id
}
