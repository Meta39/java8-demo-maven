package com.fu.mybatisplusdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ORACLEENTITY")
public class OracleEntity {

    @TableId(type = IdType.NONE)
    private Integer id; //主键

    private String name; //姓名

}
