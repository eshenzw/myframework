package com.myframework.core.token.exception;

/**
 * Created by zw on 2017/7/22.
 */
public class TokenException extends Exception {
    private int code;

    public TokenException() {
        super();
    }

    public TokenException(String message) {
        super(message);
    }

    public TokenException(int code, String message) {
        super(message);
        this.code = code;
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public TokenException(Throwable cause) {
        super(cause);
    }

    public TokenException(Exception e,String message){
        super(message,e.getCause());

    }

    /**
     * 获取异常描述信息 *
     *
     * @return 描述信息
     */
    @Override
    public String getMessage() {
        String msg = super.getMessage();
        if (getCause() != null) {
            msg = msg + ": " + getCause().getMessage();
        }
        return msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
