package com.fu.springbootdemo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文章 实体类
 *
 * @author Meta39
 * @since 2023-04-15 23:06:07
 */
@Data
@TableName("article")
public class Article implements Serializable {
    private static final long serialVersionUID = -89717370483188616L;
    @TableId(type = IdType.AUTO, value = "id")
    private Long id; //ID

    @TableField("user_id")
    private Integer userId; //用户ID

    @TableField("title")
    private String title; //文章标题

    @TableField("content")
    private String content; //文章内容

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime; //创建时间

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime; //更新时间

}
