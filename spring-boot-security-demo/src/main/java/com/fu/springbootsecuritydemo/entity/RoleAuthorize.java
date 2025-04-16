package com.fu.springbootsecuritydemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色关联权限 实体类
 *
 * @author meta39
 * @since 2023-03-13 21:49:13
 */
@Data
@TableName("role_authorize")
public class RoleAuthorize implements Serializable {
    private static final long serialVersionUID = 354708614554632195L;
    @TableId(type = IdType.AUTO, value = "id")
    private Integer id; //id

    @TableField("role_id")
    private Integer roleId; //角色id

    @TableField("authorize_id")
    private Integer authorizeId; //权限id

}
