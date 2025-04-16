package com.fu.mybatisplusdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("simple_object")
public class SimpleObject {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
}
