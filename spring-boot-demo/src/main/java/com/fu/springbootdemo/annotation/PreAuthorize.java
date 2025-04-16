package com.fu.springbootdemo.annotation;

import java.lang.annotation.*;

/**
 * 鉴权注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAuthorize {

    String value();//权限值。如：user:select表示需要用户查询权限

}
