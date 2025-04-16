package com.fu.springbootdemo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 角色
 */
@Data
@TableName("role")
public class Role implements Serializable {
    private static final long serialVersionUID = -38372391020893738L;
    @TableId(type = IdType.AUTO,value = "id")
    private Integer id; //id    

    @NotEmpty
    @TableField("role_name")
    private String roleName; //角色名称

    @TableField("description")
    private String description; //角色描述

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime; //创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime; //更新时间

    //特殊逻辑删除，0未删除，NULL为已删除。解决roleName被删除后重名的问题。
    @TableLogic(value = "0",delval = "NULL")
    @TableField("is_delete")
    private Integer isDelete; //删除（0：否 NULL：是）

    @TableField(exist = false)
    Set<Authorize> authorizes; //角色所拥有的权限集合

}
