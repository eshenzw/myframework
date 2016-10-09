package com.myframework.annotation;

import java.lang.annotation.*;

/**
 * 服务名称
 * 
 * @author AndyFan*
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(
{
	ElementType.TYPE
})
public @interface ServiceName
{
	/** 数组集合 */
	String value();
}
