package com.myframework.core.error.exception;

import com.myframework.core.error.exception.code.impl.BaseCode;
import com.myframework.core.error.exception.internel.ErrorCodeLoader;
import org.apache.commons.lang3.StringUtils;

/**
 * 业务通用异常
 *
 * created by zw
 */
public class BizException extends RuntimeException {

    private static final long serialVersionUID = -7924878864085184620L;

    /**
     * 错误码
     */
    private int errorCode;

    /**
     * 抛出的异常,非预期异常，错误码为 BaseCode.EX_SYSTEM_UNKNOW
     *
     * @param cause   错误原因
     * @param message 错误信息
     */
    public BizException(Throwable cause, String message) {
        super(generateMessageWithCodeAndMsg(BaseCode.EX_SYSTEM_UNKNOW.getCode(), message), cause);
        this.errorCode = BaseCode.EX_SYSTEM_UNKNOW.getCode();
    }

    /**
     * 抛出的异常
     *
     * @param cause 源异常
     */
    public BizException(Throwable cause) {

        super(generateUnknowMessageWithCode(), cause);

        this.errorCode = BaseCode.EX_SYSTEM_UNKNOW.getCode();

    }

    /**
     * 抛出的异常
     *
     * @param errorCode 异常码
     * @param message   message
     * @param cause     源异常
     */
    public BizException(int errorCode, String message, Throwable cause) {
        super(generateMessageWithCodeAndMsg(errorCode, message), cause);
        this.errorCode = errorCode;
    }

    /**
     * 抛出的异常非预期异常
     *
     * @param message message
     * @param cause   源异常
     */
    public BizException(String message, Throwable cause) {
        super(generateMessageWithCodeAndMsg(BaseCode.EX_SYSTEM_UNKNOW.getCode(), message), cause);

        this.errorCode = BaseCode.EX_SYSTEM_UNKNOW.getCode();
    }

    /**
     * 抛出的异常
     *
     * @param message message
     * @param cause   源异常
     */
    public BizException(int errorCode, String message) {
        super(generateMessageWithCodeAndMsg(errorCode, message));
        this.errorCode = errorCode;

    }

    /**
     * 抛出的异常
     *
     * @param errorCode errorCode
     */
    public BizException(int errorCode) {
        super(generateMessageWithCode(errorCode));
        this.errorCode = errorCode;

    }

    /**
     * @param errorCode
     * @param cause
     */
    public BizException(int errorCode, Throwable cause) {
        super(generateMessageWithCode(errorCode), cause);
        this.errorCode = errorCode;

    }

    /**
     * 获取异常码
     *
     * @return String
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * 获取message
     *
     * @param code
     * @param message
     * @return
     */
    private static String generateMessageWithCodeAndMsg(int code, String message) {

        if (StringUtils.isNotEmpty(message)) {
            return message;
        } else {
            return "系统异常";
        }
//		return "[code:" + code + ",message:" + message + "]";

    }

    /**
     * @param code
     * @param message
     * @return
     */
    private static String generateMessageWithCode(int code) {
        String message = ErrorCodeLoader.getErrorMessageByCode(code);
        return generateMessageWithCodeAndMsg(code, message);

    }

    /**
     * @param code
     * @param message
     * @return
     */
    private static String generateUnknowMessageWithCode() {
        int code = BaseCode.EX_SYSTEM_UNKNOW.getCode();
        String message = ErrorCodeLoader.getErrorMessageByCode(code);
        return generateMessageWithCodeAndMsg(code, message);

    }

}