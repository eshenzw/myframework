package com.myframework.annotation;

import java.lang.annotation.*;

/**
 * 服务类型
 * 
 * @author AndyFan*
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
