package com.fu.springbootsecuritydemo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
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

    @TableField("role_name")
    private String roleName; //角色名称

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime; //创建时间

    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime; //更新时间

    //特殊逻辑删除，0未删除，NULL为已删除。解决roleName被删除后重名的问题。
    @TableLogic(value = "0",delval = "NULL")
    @TableField("is_delete")
    private Integer isDelete; //删除（0：否 NULL：是）

    @TableField(exist = false)
    Set<Authorize> authorizes; //角色所拥有的权限集合

}
