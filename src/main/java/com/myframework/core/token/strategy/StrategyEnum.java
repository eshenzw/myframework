package com.myframework.core.token.strategy;


public enum StrategyEnum {

    //校验成功
    STRATEGY_VALIDATE_SUCCESS(0),

    //校验成功，跳出校验逻辑
    STRATEGY_VALIDATE_SUCCESS_ADN_BREAK(1),;
    /**
     * 校验结果
     */
    private int code;


    private StrategyEnum(int code) {
        this.code = code;
    }


    public int getCode() {

        return code;
    }


    public void setCode(int code) {

        this.code = code;
    }

}	