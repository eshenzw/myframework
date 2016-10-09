package com.myframework.interceptor;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.myframework.base.BaseController;
import com.myframework.entity.BaseUserEntity;
import com.myframework.enums.UserTypeEnum;
import com.myframework.util.JsonUtil;
import com.myframework.util.RespUtil;
import com.myframework.util.StringUtil;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	/**
     * 登录页面的URL
     */
    private UserTypeEnum userType = UserTypeEnum.Normal;
    /**
     * 登录的页面URL  当未登录访问已登录的页面时，自动跳转到该页面
     * @param userType
     */
    public void setUserType(UserTypeEnum userType){
        this.userType = userType;
    }
    /**
     * 登录页面的URL
     */
    private String loginUrl;
    /**
     * 登录的页面URL  当未登录访问已登录的页面时，自动跳转到该页面
     * @param loginUrl
     */
    public void setLoginUrl(String loginUrl){
        this.loginUrl = loginUrl;
    }
    /**
     * 利用正则映射到需要拦截的路径
     */
    private String[] regexUrls;
    /**
     * 利用正则映射到需要拦截的路径
     * @param regexUrls
     */
    public void setRegexUrls(String[] regexUrls) {
        this.regexUrls = regexUrls;
    }
    /**
     * 在Controller方法前进行拦截
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果handler controller实现了接口，则设置上下文
        BaseUserEntity loginUser = null;
        //BaseController base = (BaseController)handler;
        if (handler != null && handler instanceof HandlerMethod){
        	HandlerMethod handlerMethod = (HandlerMethod) handler; 
        	if(handlerMethod.getBean() instanceof BaseController) {
        		//System.out.println("excute:" + handlerMethod.getBean() + " "+ handlerMethod.getMethod().getName());
                BaseController web = (BaseController)handlerMethod.getBean();
                web.setUserType(this.userType);
                web.setLoginUrl(this.loginUrl);
        		loginUser = web.getSessionUser();
        	}  
        }
        
        String strUrl = request.getRequestURI();
        if (loginUser == null && !StringUtil.isNullOrEmpty(strUrl) && regexUrls != null && regexUrls.length >0){
            for(String regex : regexUrls){
                if (StringUtil.isNullOrEmpty(regex)){
                    continue;
                }
                if (strUrl.matches(regex)){ //当前页面需要登录
                    String strToUrl = loginUrl + "?returnUrl=" + URLEncoder.encode(strUrl, "utf-8");
                    if ("GET".equalsIgnoreCase(request.getMethod())){
                        response.sendRedirect(strToUrl);//转到登录页    
                    }else{
                        //Json返回数据
                    	System.out.println("need login!");
                        RespUtil.responseJson(JsonUtil.errMsg("请先登录！"));
                    }
                    return false;
                }
            }
        }
        return true;
    }
	
}
