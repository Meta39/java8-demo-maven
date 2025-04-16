package com.fu.springbootdemo.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fu.springbootdemo.converter.SexConverter;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户
 */
@Data
@TableName("user")
@JsonIgnoreProperties(allowSetters = true, value = {"pwd"})//盐、密码不允许序列化成json字符串返回给前端
public class User implements Serializable {
    private static final long serialVersionUID = -45656328374893341L;
    @TableId(type = IdType.AUTO,value = "id")
    @ExcelIgnore //不导出Excel
    private Integer id; //id    

    @NotEmpty
    @TableField("username")
    @ExcelProperty("用户名")
    private String username; //用户名

    @TableField("pwd")
    @ExcelIgnore //不导出Excel
    private String pwd; //密码    

    @NotEmpty
    @TableField("nickname")
    @ExcelProperty("昵称")
    private String nickname; //昵称    

    @NotNull
    @TableField("sex")
    @ExcelProperty(value = "性别", converter = SexConverter.class)
    private Integer sex; //性别（0：女 1：男）

    @TableField("remark")
    private String remark; //备注

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    @ExcelProperty(value = "创建时间")
    private LocalDateTime createTime; //创建时间

    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    @ExcelProperty(value = "更新时间")
    private LocalDateTime updateTime; //更新时间

    @TableField("last_login_time")
    @ExcelProperty(value = "最后登录时间")
    private LocalDateTime lastLoginTime; //最后登录时间

    @NotNull
    @TableField("is_ban")
    @ExcelProperty(value = "禁用（0：否 1：是）")
    private Integer isBan; //禁用（0：否 1：是）

    //特殊逻辑删除，0未删除，NULL为已删除。数据库username和is_delete为组合唯一索引，解决逻辑删除后username同名唯一索引的问题
    @TableLogic(value = "0",delval = "NULL")
    @TableField("is_delete")
    @ExcelIgnore //不导出Excel
    private Integer isDelete; //删除（0：否 1：是）

    @TableField(exist = false)
    @ExcelIgnore //不导出Excel
    private Set<Role> roles; //用户角色ID集合
}
