package com.myframework.core.filter;

import com.myframework.core.common.utils.LocalhostIpFetcher;
import com.myframework.core.db.multi.DataSourceHolder;
import com.myframework.core.entity.BaseUserEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * Request Filter 作用是将request,response设置到ThreadLocal中。
 * 同一个request请求处理线程可以通过getRequestLocal(),getResponseLocal()
 * 获得当前的request和response值。
 *
 * @author zhaowei
 */
public class RequestFilter implements Filter {
    public final static String HOST_URL = "hostUrl";
    public final static String CONTEXT_PATH = "contextPath";
    public final static String BASE_PATH = "basePath";
    /***/
    private static transient Log log = LogFactory.getLog(RequestFilter.class);
    /***/
    private FilterConfig filterConfig = null;
    /***/
    private static final ThreadLocal<HttpServletRequest> requestLocal = new ThreadLocal<HttpServletRequest>();
    /***/
    private static final ThreadLocal<HttpServletResponse> responseLocal = new ThreadLocal<HttpServletResponse>();
    /***/
    private static final ThreadLocal<String> currentToken = new ThreadLocal<String>();
    /*本机网卡ip*/
    private static String localIp = LocalhostIpFetcher.fetchLocalIP();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) {
        try {
            if (log.isDebugEnabled()) {
                //注释掉否则有大量的日志积压
                //log.debug("enter RequestFilter doFilter");
            }
            String path = request.getServletContext().getContextPath();
            String host = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String basePath = host + path;
            request.setAttribute(HOST_URL, host);
            request.setAttribute(CONTEXT_PATH, path);
            request.setAttribute(BASE_PATH, basePath);
            requestLocal.set((HttpServletRequest) request);
            responseLocal.set((HttpServletResponse) response);
            filterChain.doFilter(new XSSRequestWrapper((HttpServletRequest) request), response);
        } catch (ServletException sx) {
            filterConfig.getServletContext().log(sx.getMessage());
        } catch (IOException iox) {
            filterConfig.getServletContext().log(iox.getMessage());
        } finally {
            clearThreadLocal();
        }
    }

    @Override
    public void destroy() {
        clearThreadLocal();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    /**
     * 获取当前线程的request
     *
     * @return the requestLocal
     */
    public static HttpServletRequest getRequest() {
        HttpServletRequest request = requestLocal.get();
        if (request == null) {
            //注释掉否则有大量的日志积压
            //log.warn("request在线程内没有找到,可能原因：1、当前调用没有处在web容器内；2、web.xml没有配置RequestFilter过滤器；");
        }
        return request;
    }

    /**
     * 获取当前线程的reponse
     *
     * @return the responseLocal
     */
    public static HttpServletResponse getResponse() {
        HttpServletResponse response = responseLocal.get();
        if (response == null) {
            //注释掉否则有大量的日志积压
            //log.warn("response在线程内没有找到,可能原因：1、当前调用没有处在web容器内；2、web.xml没有配置RequestFilter过滤器；");
        }
        return response;
    }

    /**
     * 获取当前线程的session
     *
     * @return the requestLocal.session
     */
    public static HttpSession getSession() {
        HttpServletRequest request = requestLocal.get();
        if (request == null) {
            //注释掉否则有大量的日志积压
            //log.warn("session在线程内没有找到,可能原因：1、当前调用没有处在web容器内；2、web.xml没有配置RequestFilter过滤器；");
            return null;
        }
        return request.getSession();
    }

    public static String getBasePath() {
        return (String) getRequest().getAttribute(BASE_PATH);
    }

    public static String getContextPath() {
        return (String) getRequest().getAttribute(CONTEXT_PATH);
    }

    public static void setCurrentToken(String token) {
        currentToken.set(token);
    }

    public static String getCurrentToken() {
        return currentToken.get();
    }

    public static BaseUserEntity getSessionUser() {
        // TODO Auto-generated method stub
        HttpSession session = getRequest().getSession();
        BaseUserEntity user = (BaseUserEntity)session.getAttribute(BaseUserEntity.USER_SESSION_BEAN);
        return user;
    }

    public static void setSessionUser(BaseUserEntity user) {
        // TODO Auto-generated method stub
        HttpSession session = getSession();
        if(user != null){
            session.setAttribute(BaseUserEntity.USER_SESSION_ID, user.getUserId());
            session.setAttribute(BaseUserEntity.USER_SESSION_BEAN, user);
        }else{
            session.setAttribute(BaseUserEntity.USER_SESSION_ID, null);
            session.setAttribute(BaseUserEntity.USER_SESSION_BEAN, null);
        }
    }

    /**
     *
     */
    public static void clearThreadLocal() {

        requestLocal.remove();
        responseLocal.remove();

        currentToken.remove();

        // 清理掉数据源相关的
        DataSourceHolder.clearThreadLocal();
    }

    /**
     * 打印当前的请求数据
     *
     * @return
     */
    public static void printReq() {
        // request url
        HttpServletRequest request = requestLocal.get();
        StringBuffer buf = new StringBuffer();
        buf.append("------------- print request parameters ----------------\n");
        buf.append(request.getRequestURL());
        String queryStr = request.getQueryString();
        if (queryStr != null) {
            buf.append("?").append(queryStr);
        }

        buf.append("\n");

        // request parameters
        Map map = request.getParameterMap();
        for (Iterator iter = map.entrySet().iterator(); iter.hasNext(); ) {
            Entry entry = (Entry) iter.next();
            String key = (String) entry.getKey();
            String[] values = (String[]) entry.getValue();
            buf.append(key);
            buf.append(" = ");
            buf.append(Arrays.asList(values));
            buf.append("\n");
        }

        log.debug("\n" + buf);
    }

    /**
     * 打印当前的请求url
     *
     * @return
     */
    public static void printUrl() {
        HttpServletRequest request = requestLocal.get();
        StringBuffer reqUrl = request.getRequestURL();
        String queryStr = request.getQueryString();
        if (queryStr != null) {
            reqUrl.append("?").append(queryStr);
        }
        log.debug("\n" + reqUrl);
    }

    /**
     * 打印当前的请求参数
     *
     * @return
     */
    public static void printParams() {
        HttpServletRequest request = requestLocal.get();
        Map map = request.getParameterMap();
        StringBuffer paramInfo = new StringBuffer(256);
        for (Iterator iter = map.entrySet().iterator(); iter.hasNext(); ) {
            Entry entry = (Entry) iter.next();
            String key = (String) entry.getKey();
            String[] values = (String[]) entry.getValue();
            paramInfo.append(key);
            paramInfo.append(" = ");
            paramInfo.append(Arrays.asList(values));
            paramInfo.append("\n");
        }
        log.debug("\n" + paramInfo);
    }

    /**
     * 获取当前的请求数据
     *
     * @return Map
     */
    public static Map<String, Object> getRequestParam() {
        HttpServletRequest request = requestLocal.get();
        Enumeration<String> params = request.getParameterNames();
        Map<String, Object> result = new HashMap<String, Object>();
        while (params.hasMoreElements()) {
            String key = params.nextElement();
            result.put(key, request.getParameter(key));
        }
        return result;
    }

    /**
     * 获取当前的请求参数
     *
     * @return String
     */
    public static String getRequestParams() {
        HttpServletRequest request = requestLocal.get();
        String result = "";
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String key = params.nextElement();
            result += key + "=" + request.getParameter(key) + "&";
        }
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * 获取本机IP地址
     *
     * @return 本机IP地址
     */
    public static String getLocalIp() {
        return localIp;
    }

    /**
     * 获取远程访问IP
     *
     * @return String
     */
    public static String getRemoteIp() {
        HttpServletRequest request = requestLocal.get();
        String remoteIp = request.getHeader("x-forwarded-for");
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("X-Real-IP");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("Proxy-Client-IP");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("HTTP_CLIENT_IP");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getRemoteAddr();
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getRemoteHost();
        }
        return remoteIp;
    }

}
