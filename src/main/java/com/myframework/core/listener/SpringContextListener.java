package com.myframework.core.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.myframework.util.SpringContextUtil;

public class SpringContextListener implements ServletContextListener {

	private static WebApplicationContext springContext;
    
    public SpringContextListener() {
        super();
    }
    
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// TODO Auto-generated method stub
		springContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		ServletContext context = event.getServletContext();
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		SpringContextUtil contextUtil = (SpringContextUtil) ctx.getBean("contextUtil");
		contextUtil.setContext(ctx);
	}
	
	public static ApplicationContext getApplicationContext() {
        return springContext;
    }

}
