package com.fu.springbootdemo.annotation;

import java.lang.annotation.*;

/**
 * 返回原始数据给前端
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReturnMeta {

}
