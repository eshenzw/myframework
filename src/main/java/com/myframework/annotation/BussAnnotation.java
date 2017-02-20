package com.myframework.annotation;

import java.lang.annotation.*;

/**
 * 模块注解
 *
 * @author zhaowei
 *
 */
@Retention(RetentionPolicy.RUNTIME)  
@Target({ElementType.METHOD}) 
@Inherited  
@Documented 
public @interface BussAnnotation {  
    //模块名  
    String module();  
    //操作内容  
    String option();  
} 
