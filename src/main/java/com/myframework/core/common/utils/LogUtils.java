package com.myframework.core.common.utils;

/**
 * .
 */
public class LogUtils {


    /**
     * log max length: 避免日志打印太长了
     */
    public static String shotter(Object t) {

        int maxLength = 256;

        if (t == null) {
            return null;
        }

        String toStr = t.toString();
        return toStr.substring(0, Math.min(maxLength, toStr.length()));
    }
}
