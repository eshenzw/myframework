package com.myframework.core.token.exception;

/**
 * Created by zw on 2017/7/22.
 */
public enum TokenCode {
    GENERATE_TOKEN_FAILED(1001),
    PARSE_TOKEN_FAILED(1002),
    IEAGLLE_REFRESH_TOKEN(1003),
    REFRESH_TOKEN_EXPIRE(1004),
    TOKEN_EXPIRE(1005),
    REFRESH_TOKEN_FAILED(1006),
    IEAGLLE_TOKEN(1007),
    IEAGLLE_TOKEN_EXPIRE_TIME(1008),
    IEAGLLE_TOKEN_4_SESSION_EXPIRE(1009),
    RELOGIN_BY_PWD_MODIFIED(1010),
    RELOGIN_BY_LOGIN_IN_OTHER_PLACE(1011);

    private final int code;

    private TokenCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return String.valueOf(this.code);
    }

    public int getCode() {
        return this.code;
    }
}
