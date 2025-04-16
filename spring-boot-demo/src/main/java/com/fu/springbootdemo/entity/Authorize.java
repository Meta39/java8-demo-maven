package com.fu.springbootdemo.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 权限 实体类
 *
 * @author meta39
 * @since 2023-03-14 11:48:50
 */
@Data
@TableName("authorize")
public class Authorize implements Serializable {
    private static final long serialVersionUID = -29604834339467860L;
    @TableId(type = IdType.AUTO, value = "id")
    private Integer id; //id

    @TableField("p_id")
    private Integer pId; //parentId

    @TableField("authorize_name")
    private String authorizeName; //权限名称（后端授权）

    @NotEmpty(message = "菜单名称不能为空")
    @TableField("menu_name")
    private String menuName;// 菜单中文名称（前端授权）

    @TableField("name")
    private String name; //Vue页面名称（前端）

    @TableField("path")
    private String path; //Vue页面路径（前端）

    @TableField("node_type")
    private Integer nodeType; //节点类型（1：文件夹 2：页面 3：按钮）

    @TableField("sort")
    private Integer sort; //排序号

    @TableField("level")
    private Integer level; //层次

    //特殊逻辑删除，0未删除，NULL为已删除。解决权限名称重名问题。
    @TableLogic(value = "0",delval = "NULL")
    @TableField("is_delete")
    private Integer isDelete; //删除（0：否 NULL：是）

    @TableField(exist = false)
    List<Authorize> childAuthorize; //子节点权限信息

}
