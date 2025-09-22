package com.fu.mybatisplusdemo.dto;

import com.fu.mybatisplusdemo.enums.SexEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserDTO implements Serializable {
    private Integer id; //主键ID
    private String name; //用户名
    private SexEnum sex; //性别（0：女 1：男）用枚举类型存储
    private String nickName; //昵称
    private Date createTime; //创建时间
    private Date updateTime; //修改时间
    private Integer isDeleted; //逻辑删除（0：否 1：是）
}
