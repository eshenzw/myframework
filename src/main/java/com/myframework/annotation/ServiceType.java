package com.myframework.annotation;

import java.lang.annotation.*;

/**
 * 服务类型
 * 
 * @author zhaowei*
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(
{
	ElementType.TYPE
})
public @interface ServiceType
{
	/** 数组集合 */
	SType value();
}
