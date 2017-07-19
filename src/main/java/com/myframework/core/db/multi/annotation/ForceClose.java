package com.myframework.core.db.multi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zw
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ForceClose {
    /**
     * 强制执行完关闭数据源连接
     */
    boolean value() default true;
}
