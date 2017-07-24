package com.myframework.core.common.view;

import com.myframework.core.error.exception.BizException;
import com.myframework.core.error.exception.code.impl.BaseCode;

/**
 * 
 * created by zw
 *
 */
public class BizResult {
	
	public static final int CODE_SUCCESS = 200;
	
	/**
	 * 返回码，除了200，其他都是异常码
	 */
	private int code;
	
	/**
	 * 返回提示信息
	 */
	private String message;
	
	/**
	 * 返回数据
	 */
	private Object data;
	

	private BizResult(int code, Object data, String message) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public String getMessage() {
		return message==null?"":message;
	}

	public Object getData() {
		return data==null?"":data;
	}

	public boolean isSuccess() {
		return CODE_SUCCESS == code;
	}
	
	public int getCode() {
		return this.code;
	}
	public static final BizResult success() {
		return new BizResult(CODE_SUCCESS, null, null);
	}

	public static final BizResult success(Object data) {
		return new BizResult(CODE_SUCCESS, data, null);
	}

	public static final BizResult success(Object data, String message) {
		return new BizResult(CODE_SUCCESS, data, message);
	}
	

	public static final BizResult failed(int code, String message) {
		return new BizResult(code, null, message);
	}

	public static final BizResult failed(BizException e) {
		return new BizResult(e.getErrorCode(), null, e.getMessage());
	}
	

	public static final BizResult failed(Exception e, String message) {
		return new BizResult(BaseCode.EX_SYSTEM_UNKNOW.getCode(), null, message);
	}
	
	
	
	
}
