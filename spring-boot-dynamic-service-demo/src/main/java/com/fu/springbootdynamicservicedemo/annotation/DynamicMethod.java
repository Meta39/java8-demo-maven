package com.fu.springbootdynamicservicedemo.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicMethod {

    String value() default "";

}
