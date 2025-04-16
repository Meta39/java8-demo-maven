package com.fu.springbootdemo.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;

/**
 * 用户关联角色 实体类
 * @author meta39
 * @since 2023-03-13 21:48:40
 */
@Data
@TableName("user_role")
public class UserRole implements Serializable{
    private static final long serialVersionUID = 103518799854629370L;
        @TableId(type = IdType.AUTO,value = "id")
        private Integer id; //id    
    
        @TableField("user_id")
        private Integer userId; //用户ID    
    
        @TableField("role_id")
        private Integer roleId; //角色ID    
    
}
