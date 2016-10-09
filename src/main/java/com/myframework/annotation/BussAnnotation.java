package com.myframework.annotation;

import java.lang.annotation.*;

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
