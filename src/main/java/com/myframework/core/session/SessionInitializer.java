package com.myframework.core.session;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

import com.myframework.constant.Constants;
import com.myframework.util.PropertiesUtil;
import com.myframework.util.StringUtil;

/**
 * Created by zw on 2015/9/9.
 */
public class SessionInitializer extends AbstractHttpSessionApplicationInitializer {

    public SessionInitializer()
    {
        super(SpringSessionConfig.class);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        if(StringUtil.toBoolean(PropertiesUtil.getInstance(Constants.MY_FRAMEWORK_FILE_PATH).getValue(SpringSessionConfig.SESSION_REDIS_ENABLE))){
            super.onStartup(servletContext);
        }
    }
}
