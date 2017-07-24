package com.myframework.core.error.exception.code.impl;


import com.myframework.core.error.exception.code.ErrorCode;
import com.myframework.core.error.exception.internel.ErrorCodeLoader;

public enum BaseCode implements ErrorCode {
	
	
	
	//成功
	EX_SYSTEM_SUCCESS(1000),
	
	//系统未知异常
	EX_SYSTEM_UNKNOW(1100),

	//没有设置异常错误码
	EX_SYSTEM_ERROR_MESSAGE_NOT_SET(1101),

	;

	

    /**
     * 异常码
     */
    private final int code;
    
    
    private BaseCode(int code){
        this.code = code;
    }
	

	@Override
	public String getMessage() {
		
		return ErrorCodeLoader.getErrorMessageByCode(this.code);
	}

	@Override
	public int getCode() {
		return code;
	}
	
	
	
}	