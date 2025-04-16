package com.fu.basedemo.annotation;

/**
 * @since 2024-07-25
 */
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String value() default "";
}

