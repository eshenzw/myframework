package com.myframework.core.token.annotation;

import java.lang.annotation.*;

/**
 * 忽略Token验证
 * @author zw
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreTokenAuth {

}
