package com.fu.springbootsecuritydemo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
@TableName("user")
@JsonIgnoreProperties(allowSetters = true, value = {"password"})//盐、密码不允许序列化成json字符串返回给前端
public class User implements Serializable {

    private static final long serialVersionUID = 8020157434124014604L;
    @TableId(type = IdType.AUTO,value = "id")
    private Integer id; //id

    @TableField("username")
    private String username; //用户名

    @TableField("password")
    private String password; //密码

    @TableField("nickname")
    private String nickname; //昵称

    @TableField("sex")
    private Integer sex; //性别（0：女 1：男）

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime; //创建时间

    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime; //更新时间

    @TableField("last_login_time")
    private Date lastLoginTime; //最后登录时间

    @TableField("is_ban")
    private Integer isBan; //禁用（0：否 1：是）

    //特殊逻辑删除，0未删除，NULL为已删除。数据库username和is_delete为组合唯一索引，解决逻辑删除后username同名唯一索引的问题
    @TableLogic(value = "0",delval = "NULL")
    @TableField("is_delete")
    private Integer isDelete; //删除（0：否 1：是）

    @TableField(exist = false)
    private Set<Role> roles; //用户角色ID集合

}
