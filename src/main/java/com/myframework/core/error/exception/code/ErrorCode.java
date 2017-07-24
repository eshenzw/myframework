package com.myframework.core.error.exception.code;


/**
 *
 * 
 */
public interface ErrorCode {

    /**
     * 设置错误内容
     * @param content
     */
    public String getMessage()  ;

    /**
     * 获取错误码
     * @return 错误码
     */
    public int getCode();
}
