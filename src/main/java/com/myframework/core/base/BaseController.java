package com.myframework.core.base;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.httpclient.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.alibaba.fastjson.JSONObject;
import com.myframework.constant.Constants;
import com.myframework.core.entity.BaseUserEntity;
import com.myframework.enums.UserTypeEnum;
import com.myframework.core.filter.RequestFilter;
import com.myframework.util.StringUtil;

public abstract class BaseController {

	private static final Logger log = LoggerFactory.getLogger(BaseController.class);
    /*********************获取Request与Response*******************/
    /**
     * 请求上下文
     */
    private HttpServletRequest request;
    /**
     * 应答上下文
     */
    private HttpServletResponse response;
    /**
     * 校验当前登录用户的用户类型
     */
    private UserTypeEnum userType;
    /**
     * 登录的页面  当访问需要登录的页面时，自动转到该页面
     */
    private String loginUrl;
    /**
     * 当前的请求对象
     * @return
     */
    protected HttpServletRequest getRequest(){
        return RequestFilter.getRequest();
    }
    /**
     * 获取当前的应答对象
     * @return
     */
    protected HttpServletResponse getResponse(){
        return RequestFilter.getResponse();
    }
    /*********************获取Request与Response*******************/
    
    /*********************用户登录相关*******************/

	public BaseUserEntity getSessionUser() {
		// TODO Auto-generated method stub
    	HttpSession session = this.getRequest().getSession();
        BaseUserEntity user = (BaseUserEntity)session.getAttribute(BaseUserEntity.USER_SESSION_BEAN);
		return user;
	}

	public void setSessionUser(BaseUserEntity user) {
		// TODO Auto-generated method stub
		HttpSession session = this.getRequest().getSession();
		if(user != null){
			session.setAttribute(BaseUserEntity.USER_SESSION_ID, user.getUserId());
			session.setAttribute(BaseUserEntity.USER_SESSION_BEAN, user);
		}else{
			session.setAttribute(BaseUserEntity.USER_SESSION_ID, null);
			session.setAttribute(BaseUserEntity.USER_SESSION_BEAN, null);
		}
	}

	public void setCookieUserId(String id) {
		// TODO Auto-generated method stub
		this.setCookie(BaseUserEntity.USER_COOKIE_ID, id);
	}

    public String getCookieUserId(){
    	String cookieId = this.getCookieValue(BaseUserEntity.USER_COOKIE_ID);
        return StringUtil.isNullOrEmpty(cookieId) ? "-100" : cookieId;
    }
	/**
     * 判断用户是否已经cookie记录
     * @return
     */
    protected boolean isLogged(){
        return (getCookieUserId() != null && Integer.valueOf(getCookieUserId())>0);
    }
    /**
     * 判断用户是否已经登录
     * @return
     */
    protected boolean isLogin(){
        return (getSessionUser() != null);
    }
    /**
     * 跳转到登录页面
     * @return
     */
    protected ModelAndView toLoginView(){
        return new ModelAndView(new RedirectView(this.loginUrl), "tourl", this.getRequest().getRequestURI());
    }
    /*********************用户登录相关*******************/

    /**
     * 获取城市id，比如南京返回3201
     */
    public String getCityId() {
        if(this.getRequest().getSession() == null) {
            return "3201";
        }
        String cityId = "3201";
        if(this.getRequest().getSession().getAttribute(Constants.SESSION_CITYID_KEY) != null){
            cityId = (String)this.getRequest().getSession().getAttribute(Constants.SESSION_CITYID_KEY);
        }
        return cityId;
    }
    /**
     * 获取城市id，比如南京返回3201
     */
    public String getCity() {
        String city = "南京";
        if(this.getRequest().getSession() != null && this.getRequest().getSession().getAttribute(Constants.SESSION_CITY_KEY) != null){
            city = (String)this.getRequest().getSession().getAttribute(Constants.SESSION_CITY_KEY);
        }
        return city;
    }

    /*********************获取访问IP*******************/
    /**
     * 获取远程访问IP
     */
    private String remoteIp = null;
    /**
     * 获取远程访问IP
     * @return
     */
    protected String getRemoteIp(){
        if (this.remoteIp==null || this.remoteIp.length()==0)
        {
            this.remoteIp = RequestFilter.getRemoteIp();
        }
        return remoteIp;
    }
    /*********************获取访问IP*******************/
    /*********************获取访问参数*******************/
    /**
     * 获取所有参数
     * @return
     */
    protected Map<String,String[]> getParams(){
        HttpServletRequest request = this.getRequest();
        return request.getParameterMap();
    }
    /**
     * 获取指定的配置
     * @param name
     * @return
     */
    protected String getParam(String name){
        return getParam(name, "");
    }
    /**
     * 根据参数名称获取参数值，如果没有找到则以默认值返回
     * @param name
     * @param defaultValue
     * @return
     */
    protected String getParam(String name, String defaultValue){
        HttpServletRequest request = this.getRequest();
        String strValue = request.getParameter(name);
        return strValue == null ? defaultValue : strValue;
    }
    /**
     * 获取整形的参数值
     * @param name
     * @return
     */
    protected int getIntParam(String name){
        return getParam(name, 0);
    }
    /**
     * 获取整形的参数值
     * @param name
     * @param defaultValue
     * @return
     */
    protected int getParam(String name, Integer defaultValue){
        String strValue = getParam(name, defaultValue.toString());
        try{
            return Integer.valueOf(strValue);
        }
        catch(Exception e){
            return defaultValue;
        }
    }
    /**
     * 获取长整形的参数值
     * @param name
     * @return
     */
    protected long getLongParam(String name){
        return getParam(name, 0L);
    }
    /**
     * 获取长整形的参数值
     * @param name
     * @param defaultValue
     * @return
     */
    protected long getParam(String name, Long defaultValue){
        String strValue = getParam(name, defaultValue.toString());
        try{
            return Long.valueOf(strValue);
        }
        catch(Exception e){
            return defaultValue;
        }
    }
    /**
     * 获取单精度的参数值
     * @param name
     * @return
     */
    protected float getFloatParam(String name){
        return getParam(name, 0F);
    }
    /**
     * 获取单精度的参数值
     * @param name
     * @param defaultValue
     * @return
     */
    protected float getParam(String name, Float defaultValue){
        String strValue = getParam(name, defaultValue.toString());
        try{
            return Float.valueOf(strValue);
        }
        catch(Exception e){
            return defaultValue;
        }
    }
    /**
     * 获取双精度的参数值
     * @param name
     * @return
     */
    protected double getDoubleParam(String name){
        return getParam(name, 0D);
    }
    /**
     * 获取双精度的参数值
     * @param name
     * @param defaultValue
     * @return
     */
    protected double getParam(String name, Double defaultValue){
        String strValue = getParam(name, defaultValue.toString());
        try{
            return Double.valueOf(strValue);
        }
        catch(Exception e){
            return defaultValue;
        }
    }
    /**
     * 获取字节的参数值
     * @param name
     * @return
     */
    protected byte getByteParam(String name){
        return getParam(name, (byte)0);
    }
    /**
     * 获取字节的参数值
     * @param name
     * @param defaultValue
     * @return
     */
    protected byte getParam(String name, Byte defaultValue){
        String strValue = getParam(name, defaultValue.toString());
        try{
            return Byte.valueOf(strValue);
        }
        catch(Exception e){
            return defaultValue;
        }
    }
    /**
     * 获取字节的参数值
     * @param name
     * @return
     */
    protected short getShortParam(String name){
        return getParam(name, (short)0);
    }
    /**
     * 获取字节的参数值
     * @param name
     * @param defaultValue
     * @return
     */
    protected short getParam(String name, Short defaultValue){
        String strValue = getParam(name, defaultValue.toString());
        try{
            return Short.valueOf(strValue);
        }
        catch(Exception e){
            return defaultValue;
        }
    }
    /**
     * 获取布尔的参数值
     * @param name
     * @return
     */
    protected boolean getBooleanParam(String name){
        return getParam(name, false);
    }
    /**
     * 获取布尔的参数值
     * @param name
     * @param defaultValue
     * @return
     */
    protected boolean getParam(String name, Boolean defaultValue){
        String strValue = getParam(name, defaultValue.toString());
        try{
            return Boolean.valueOf(strValue);
        }
        catch(Exception e){
            return defaultValue;
        }
    }
    /**
     * 获取日期的参数值
     * @param name
     * @return
     */
    protected Date getDateParam(String name){
        return getParam(name, new Date());
    }
    /**
     * 获取日期的参数值
     * @param name
     * @param defaultValue
     * @return
     */
    protected Date getParam(String name, Date defaultValue){
        String strValue = getParam(name);
        if (strValue == null || strValue.length() == 0)
            return defaultValue;
        try{
            return DateUtil.parseDate(strValue);
        }
        catch(Exception e){
            return defaultValue;
        }
    }
    /**
     * 获取Map的参数值
     * @param name
     * @return
     */
    protected Map<String,Object> getMapParam(String name){
        return getParam(name, new HashMap<String,Object>());
    }
    /**
     * 获取Map的参数值
     * @param name
     * @param defaultValue
     * @return
     */
    protected Map<String,Object> getParam(String name, HashMap<String,Object> defaultValue){
        HttpServletRequest request = this.getRequest();
        Set<String> keys = getParams().keySet();
        boolean hasMap = false;
        for (String key: keys) {
            if(key.contains(name+".")){
                hasMap = true;
            }
        }
        if (!hasMap)
            return defaultValue;
        try{
            HashMap<String,Object> ret = new HashMap<String,Object>();
            for (String key: keys) {
                if(key.contains(name+".")){
                    String subkey = key.substring(key.indexOf(".")+1);
                    ret.put(subkey,getParam(key));
                }
            }
            return ret;
        }
        catch(Exception e){
            return defaultValue;
        }
    }
    /**
     * 获取JSON的参数值
     * @param name
     * @return
     */
    protected JSONObject getJsonParam(String name){
        return getParam(name, new JSONObject());
    }
    /**
     * 获取JSON的参数值
     * @param name
     * @param defaultValue
     * @return
     */
    protected JSONObject getParam(String name, JSONObject defaultValue){
        String strValue = getParam(name);
        if (strValue == null || strValue.length() == 0)
            return defaultValue;
        try{
            return JSONObject.parseObject(strValue);
        }
        catch(Exception e){
            return defaultValue;
        }
    }
    /*********************获取访问参数*******************/
    /*******************操作Cookie********************/
    /**
     * 获取指定键的Cookie
     * @param cookieName
     * @return 如果找到Cookie则返回 否则返回null
     */
    protected Cookie getCookie(String cookieName){
        if (cookieName.isEmpty() || cookieName.equals("") || this.getRequest().getCookies() == null)
            return null;
        for(Cookie cookie : this.getRequest().getCookies()){
            if (cookieName.equals(cookie.getName()))
                return cookie;
        }
        return null; 
    }
    /**
     * 获取指定键的Cookie值
     * @param cookieName
     * @return 如果找到Cookie则返回 否则返回null
     */
    protected String getCookieValue(String cookieName){
        Cookie cookie = this.getCookie(cookieName);
        return cookie == null ? null : cookie.getValue();
    }
    /**
     * 删除指定的Cookie
     * @param cookieName
     */
    protected void removeCookie(String cookieName){
        HttpServletResponse response = this.getResponse();
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
    /**
     * 保存一个对象到Cookie里   Cookie只在会话内有效
     * @param cookieName
     * @param inst
     */
    protected void setCookie(String cookieName, Object inst){
        this.setCookie(cookieName, "/", inst);
    }
    /**
     * 保存一个对象到Cookie  Cookie只在会话内有效
     * @param cookieName
     * @param path
     * @param inst
     */
    protected void setCookie(String cookieName, String path, Object inst){
        if (StringUtil.isNullOrWhiteSpace(cookieName) || inst == null)
            return;
        String strCookieString = this.object2CookieString(inst);
        this.setCookie(cookieName, path, strCookieString);
    }
    /**
     * 保存一个对象到Cookie
     * @param cookieName
     * @param inst
     * @param expiry (秒)设置Cookie的有效时长， 负数不保存，0删除该Cookie 
     */
    protected void setCookie(String cookieName, Object inst, int expiry){
        this.setCookie(cookieName, "/", inst, expiry);
    }
    /**
     * 保存一个对象到Cookie
     * @param cookieName
     * @param path
     * @param inst
     * @param expiry (秒)设置Cookie的有效时长， 负数不保存，0删除该Cookie 
     */
    protected void setCookie(String cookieName, String path, Object inst, int expiry){
        if (StringUtil.isNullOrWhiteSpace(cookieName) || inst == null || expiry < 0)
            return;
        String strCookieString = this.object2CookieString(inst);
        this.setCookie(cookieName, path, strCookieString, expiry);
    }
    /**
     * 保存一个对象到Cookie里   Cookie只在会话内有效
     * @param cookieName
     * @param cookieValue
     */
    protected void setCookie(String cookieName, String cookieValue){
        this.setCookie(cookieName, "/", cookieValue);
    }
    /**
     * 保存一个对象到Cookie  Cookie只在会话内有效
     * @param cookieName
     * @param path
     * @param cookieValue
     */
    protected void setCookie(String cookieName, String path, String cookieValue){
        HttpServletResponse response = this.getResponse();
        if (StringUtil.isNullOrWhiteSpace(cookieName) || cookieValue == null)
            return;
        Cookie cookie = new Cookie(cookieName, cookieValue);
        if (!StringUtil.isNullOrWhiteSpace(path)){
            cookie.setPath(path);
        }
        response.addCookie(cookie);
    }
    /**
     * 保存一个对象到Cookie
     * @param cookieName
     * @param cookieValue
     * @param expiry (秒)设置Cookie的有效时长， 负数不保存，0删除该Cookie 
     */
    protected void setCookie(String cookieName, String cookieValue, int expiry){
        this.setCookie(cookieName, "/", cookieValue, expiry);
    }
    /**
     * 保存一个对象到Cookie
     * @param cookieName
     * @param path
     * @param cookieValue
     * @param expiry (秒)设置Cookie的有效时长， 负数不保存，0删除该Cookie 
     */
    protected void setCookie(String cookieName, String path, String cookieValue, int expiry){
        if (StringUtil.isNullOrWhiteSpace(cookieName) || cookieValue == null || expiry < 0)
            return;
        HttpServletResponse response = this.getResponse();
        if (StringUtil.isNullOrWhiteSpace(cookieName) || cookieValue == null)
            return;
        Cookie cookie = new Cookie(cookieName, cookieValue);
        if (!StringUtil.isNullOrWhiteSpace(path)){
            cookie.setPath(path);
        }
        cookie.setMaxAge(expiry);
        response.addCookie(cookie);
    }
    /**
     * 把对象转换为Cookie存贮字串
     * @param inst
     * @return
     */
    private String object2CookieString(Object inst){
        if (inst == null)
            return "";
        StringBuilder strCookieValue = new StringBuilder();
        for(java.lang.reflect.Field field : inst.getClass().getDeclaredFields()){
            try{
                if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) || 
                        java.lang.reflect.Modifier.isFinal(field.getModifiers())){
                    continue;
                }    
                if (!this.isSimpleProperty(field.getType())) continue;//不是元数据
                field.setAccessible(true);// 提升权限
                Object objValue = field.get(inst);
                String strValue;
                if (field.getType().equals(Date.class)){
                    strValue = DateUtil.formatDate((Date)objValue);
                }else{
                    strValue = objValue == null ? "" : objValue.toString();
                }
                if (strCookieValue.length() > 0){
                    strCookieValue.append(String.format("&%s=%s", field.getName(), URLEncoder.encode(strValue,"UTF-8")));
                }
                else{
                    strCookieValue.append(String.format("%s=%s", field.getName(), URLEncoder.encode(strValue,"UTF-8")));
                }    
            }
            catch(Exception e){
            
                log.error("object2CookieString faild",e);
                continue;
            }
        }
        return strCookieValue.toString();
    }
    /**
     * 从Cookie中获对对象
     * @param cookieName
     * @param inst
     * @return 如果获取转换成功，则返回true, 否则返回false
     */
    protected boolean getCookieObject(String cookieName, Object inst){
        if (inst == null){
            return false;
        }
        String cookieValue = this.getCookieValue(cookieName);
        if (cookieValue == null){
            return false;
        }
        for(java.lang.reflect.Field field : inst.getClass().getDeclaredFields()){
            try{
                if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) || 
                        java.lang.reflect.Modifier.isFinal(field.getModifiers())){
                    continue;
                }
                if (!this.isSimpleProperty(field.getType())) continue;//不是元数据
                field.setAccessible(true);// 提升权限
                
                Pattern pattern = Pattern.compile(String.format("(^|&)%s=([^(&|$)]+)", field.getName()));
                Matcher matcher = pattern.matcher(cookieValue);
                String strValue = "";
                if (matcher.find()){
                    strValue = matcher.group(2);
                    strValue = URLDecoder.decode(strValue, "UTF-8");
                }
                field.set(inst, ConvertUtils.convert(strValue, field.getType()));
            }
            catch(Exception e){
                log.error("getCookieObject faild", e);
                return false;
            }
        }
        return true;
    }
    /**
     * 是否是简单的数据类型
     * @param propType
     * @return
     */
    private boolean isSimpleProperty(Class<?> propType){
        if (!propType.isPrimitive() && !propType.isEnum() && (!propType.equals(String.class) && !propType.equals(Date.class)))
        {
            return false;
        }
        return true;
    }
    /*******************操作Cookie********************/
    public UserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(UserTypeEnum userType) {
        this.userType = userType;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
}
