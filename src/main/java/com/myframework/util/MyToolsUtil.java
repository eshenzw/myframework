/* author hp
 * 创建日期 May 30, 2011
 */
package com.myframework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;


@SuppressWarnings("unchecked")
public class MyToolsUtil {

	static Logger log = LoggerFactory.getLogger(MyToolsUtil.class);
	
	/**
	 * 从对象中取属性
	 * 
	 * @param obj
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static Object getValueFromObject(Object obj, String name) throws Exception {
		return getValueFromObject(obj, name, null);
	}

	/**
	 * 从对象中取属性
	 * 
	 * @param obj
	 * @param name
	 * @param defaultValue
	 * @return
	 * @throws Exception
	 */
	public static Object getValueFromObject(Object obj, String name, Object defaultValue) throws Exception {
		if (obj == null || StringUtil.isEmpty(name)) {
			return defaultValue;
		}
		Object result = null;
		String[] arr = name.split("[.]");
		result = getProperty(obj, arr[0], defaultValue);
		for (int i = 1; i < arr.length; i++) {
			if (result == null) {
				return defaultValue;
			}
			if (result instanceof String) {
				return result.toString();
			} else {
				result = getProperty(result, arr[i], defaultValue);
			}
		}
		return result;
	}

	/**
	 * 从request中取值，先getParameter再getAttribute最后取request.getSession().
	 * getAttribute
	 * 
	 * @param request
	 * @param param
	 * @return
	 */
	public static Object getValueFromRequest(ServletRequest request, String param) {
		return getValueFromRequest(request, param, null);
	}

	/**
	 * 从request中取值，先getParameter再getAttribute最后取request.getSession().
	 * getAttribute
	 * 
	 * @param request
	 * @param param
	 * @param defaultValue
	 * @return
	 */
	public static Object getValueFromRequest(ServletRequest request, String param, Object defaultValue) {
		if (request == null || param == null) {
			return defaultValue;
		}
		Object obj = request.getAttribute(param);
		if (obj == null) {
			obj = request.getParameter(param);
		}
		if (obj == null) {
			HttpServletRequest req = (HttpServletRequest) request;
			obj = req.getSession().getAttribute(param);
		}
		if (obj == null) {
			obj = defaultValue;
		}
		return obj;
	}

	/**
	 * 从对象中取值，相当于beanUtils.getProperty
	 * 
	 * @param obj
	 * @param name
	 * @param defaultValue
	 * @return
	 * @throws Exception
	 */
	public static Object getProperty(Object obj, String name, Object defaultValue) throws Exception {
		Object result = null;
		if (obj == null) {
			result = null;
		} else if (obj instanceof Map) {
			result = ((Map<String, Object>) obj).get(name);
		} else if (obj instanceof ServletRequest) {
			result = getValueFromRequest((ServletRequest) obj, name, defaultValue);
		} else {
			String method = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
			result = executeJavaMethod(obj, method, null, null);
		}
		if (result == null) {
			result = defaultValue;
		}
		return result;
	}

	/**
	 * 从对象中取值，相当于beanUtils.getProperty
	 * 
	 * @param obj
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static Object getProperty(Object obj, String name) throws Exception {
		return getProperty(obj, name, null);
	}

	/**
	 * 执行方法
	 * 
	 * @param classObj
	 * @param method
	 * @param classArr
	 * @param paramArr
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object executeJavaMethod(Object classObj, String method, Class[] classArr, Object[] paramArr) throws Exception {
		if (classObj == null || StringUtil.isEmpty(method)) {
			return null;
		}
		Object result = null;
		if (classObj instanceof Class) {
			Class clazz = (Class) classObj;
			result = clazz.getMethod(method, classArr).invoke(classObj, paramArr);
		} else {
			result = classObj.getClass().getMethod(method, classArr).invoke(classObj, paramArr);
		}
		return result;
	}

}
