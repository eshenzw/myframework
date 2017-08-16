package com.myframework.core.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.myframework.core.entity.BaseUserEntity;
import com.myframework.core.filter.RequestFilter;

/**
 * Created by zw on 2017/7/24.
 */
public class ExtendConvert extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        String userInfo = "-1";
        if (RequestFilter.getSession() != null) {
            Long userId = (Long) RequestFilter.getSession().getAttribute(BaseUserEntity.USER_SESSION_ID);
            if (userId != null) {
                userInfo = userId.toString();
            }
        }
        return userInfo;
    }
}
