package com.myframework.core.log;

/**
 * Created by zw on 2017/7/26.
 */

import ch.qos.logback.classic.PatternLayout;

public class ExtendPatternLayout extends PatternLayout {
    static {
        defaultConverterMap.put("ext", ExtendConvert.class.getName());
    }
}
