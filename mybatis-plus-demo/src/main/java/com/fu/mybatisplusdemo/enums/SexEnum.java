package com.fu.mybatisplusdemo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SexEnum {
    MAN(0,"女"),
    WOMEN(1,"男");

    @EnumValue //需要在枚举类设置@EnumValue注解，并在yml配置扫描枚举类包
    private Integer sex; //设置了枚举类以后，前端直接传中文会自动转换成对应的枚举值

    @JsonValue
    private String sexCN;
}
