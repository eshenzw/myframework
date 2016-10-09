package com.myframework.util;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie记录功能认证类 <br />
 * 
 * @author Mickey Yin
 * @version 1.00, 02/14/11
 */
public class CookieUtil
{

	/**
	 * 设置cookie
	 * 
	 * @param name
	 *            名称
	 * @param value
	 *            数值
	 * @param maxAge
	 *            最大年龄
	 */
	public static void setCookie(String name, String value, int maxAge,HttpServletRequest request,HttpServletResponse response)
	{
		String charset = request.getCharacterEncoding();
		if (name != null)
		{
			try
			{
				name = java.net.URLEncoder.encode(name, charset);
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
		if (value != null)
		{
			try
			{
				value = java.net.URLEncoder.encode(value, charset);
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
		// 把null转换成empty空字符
		if (value == null)
		{
			value = "";
		}
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAge);
		cookie.setSecure(false);
		cookie.setPath(getPath(request));
		response.addCookie(cookie);
	}

	/**
	 * 删除Cookie
	 * 
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 * @param name
	 *            名称
	 */
	public static void deleteCookie(String name,HttpServletRequest request,HttpServletResponse response)
	{
		String charset = request.getCharacterEncoding();
		if (name != null)
		{
			try
			{
				name = java.net.URLEncoder.encode(name, charset);
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
		Cookie cookie = getCookie(request.getCookies(), name);
		if (cookie != null)
		{
			cookie.setPath(getPath(request));
			cookie.setValue("");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
	}

	/**
	 * 通过Cookie名称获得Cookie对象
	 * 
	 * @param cookies
	 *            cookies
	 * @param name
	 *            name
	 * @return cookie
	 */
	public static Cookie getCookie(Cookie[] cookies, String name)
	{
		Cookie cookie = null;
		if (cookies == null || name == null || name.length() == 0)
		{
			return null;
		}
		for (int i = 0; i < cookies.length; i++)
		{
			if (name.equals(cookies[i].getName()))
			{
				cookie = cookies[i];
				break;
			}
		}
		return cookie;
	}

	/**
	 * 获取当前路径
	 * 
	 * @param request
	 *            请求
	 * @return string
	 */
	private static String getPath(HttpServletRequest request)
	{
		String path = request.getContextPath();
		// 如果没有获取地址时将直接返回跟路径。
		if (path == null || path.length() == 0)
		{
			return "/";
		}
		return path;
	}
}
