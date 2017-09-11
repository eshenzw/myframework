package com.myframework.core.token.annotation;

import java.lang.annotation.*;

/**
 * Created by zw on 2017/9/11.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WithTokenAuth {
    String value();
}
