package com.fu.springbootsecuritydemo.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

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

    @TableField("authorize_name_cn")
    private String authorizeNameCn; //权限名称中文（前端展示）

    @TableField("node_type")
    private Integer nodeType; //节点类型（1：文件夹 2：页面 3：按钮）

    @TableField("icon_url")
    private String iconUrl; //图标地址

    @TableField("sort")
    private Integer sort; //排序号

    @TableField("link_url")
    private String linkUrl; //页面对应的地址

    @TableField("level")
    private Integer level; //层次

    //特殊逻辑删除，0未删除，NULL为已删除。解决权限名称重名问题。
    @TableLogic
    @TableField("is_delete")
    private Integer isDelete; //删除（0：否 NULL：是）

    @TableField(exist = false)
    List<Authorize> childAuthorize; //子节点权限信息

}
