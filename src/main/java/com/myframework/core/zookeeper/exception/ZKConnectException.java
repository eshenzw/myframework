package com.myframework.core.zookeeper.exception;

public class ZKConnectException extends RuntimeException{

    /**
     */
    private static final long serialVersionUID = 2142280490423950627L;
    
    public ZKConnectException(String message) {
        super(message);
    }

    public ZKConnectException(String message, Throwable cause) {
        super(message, cause);
    }

}
