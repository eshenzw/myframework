package com.myframework.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myframework.core.filter.RequestFilter;

/**
 * Created by zw on 2016/8/19.
 */
public class RespUtil {

    /** 日志. */
    protected static Logger logger = LoggerFactory.getLogger(RespUtil.class);
    /** plain类型 */
    private static final String CONTENT_TYPE_PLAIN = "text/plain;charset=UTF-8";
    /** html类型 */
    private static final String CONTENT_TYPE_HTML = "text/html;charset=UTF-8";
    /** xml类型 */
    private static final String CONTENT_TYPE_XML = "text/xml;charset=UTF-8";
    /** json类型 */
    private static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
    /** jsonp类型 */
    private static final String CONTENT_TYPE_JSONP = "application/json;charset=UTF-8";
    /** 字符集 */
    private static final Charset Charset_UTF8 = Charset.forName("UTF-8");

    /**
     * 向客户端返回结果
     *
     * @param content
     */
    public static void responsePlainText(String content)
    {
        response(content, CONTENT_TYPE_PLAIN);
    }

    /**
     * 向客户端返回html内容
     *
     * @param content
     */
    public static void responseHtmlText(String content)
    {
        response(content, CONTENT_TYPE_HTML);
    }

    /**
     * 向客户端返回结果
     *
     * @param content
     *            内容
     */
    public static void responseXml(String content)
    {
        response(content, CONTENT_TYPE_XML);
    }

    /**
     * 向客户端返回结果
     *
     * @param content
     *            内容
     */
    public static void responseJson(String content)
    {
        response(content, CONTENT_TYPE_JSON);
    }
    public static void responseJson(JSONObject jsonContent)
    {
        if (jsonContent == null) {
            jsonContent = new JSONObject();
        }
        responseJson(jsonContent.toString(4));
    }

    /**
     * 向客户端返回结果
     *
     * @param content
     *            内容
     */
    public static void responseJsonp(String content)
    {
        HttpServletRequest request = RequestFilter.getRequest();
        RequestFilter.getResponse().setHeader("P3P", "CP=CAO PSA OUR");
        String callback = request.getParameter("callback");
        if(!StringUtil.isEmpty(callback)){
            response(callback + "(" + content + ")", CONTENT_TYPE_JSONP);
        }else{
            response(content, CONTENT_TYPE_JSON);
        }
    }
    public static void responseJsonp(JSONObject jsonContent)
    {
        if (jsonContent == null) {
            jsonContent = new JSONObject();
        }
        responseJsonp(jsonContent.toString(4));
    }

    /**
     * 向客户端返回指定类型的内容
     *
     * @param content
     * @param contentType
     *            响应类型
     */
    public static void response(String content, String contentType)
    {
        HttpServletResponse rsp = RequestFilter.getResponse();
        rsp.setCharacterEncoding("UTF-8");
        rsp.setContentType(contentType);
        rsp.setHeader("Cache-Control", "no-cache");
        ServletOutputStream os = null;
        try
        {
            byte[] b = content.getBytes(Charset_UTF8);
            rsp.setContentLength(b.length);
            rsp.setHeader("zw-real-length", String.valueOf(b.length));
            os = rsp.getOutputStream();
            os.write(b);
            os.flush();
        }
        catch (IOException e)
        {
            logger.error("返回浏览器结果失败", e);
        }
        finally
        {
            IOUtils.closeQuietly(os);
        }
    }

    public static void responsePlainText(String content, int textStatus)
    {
        // logger.debug("向终端返回数据:{}", content);
        HttpServletResponse rsp = RequestFilter.getResponse();
        rsp.setCharacterEncoding("UTF-8");
        rsp.setContentType("text/plain;charset=UTF-8");
        rsp.setHeader("Cache-Control", "no-cache");
        PrintWriter pw = null;
        try
        {
            rsp.sendError(textStatus);
            pw = rsp.getWriter();
            pw.write(content);
            pw.flush();
        }
        catch (IOException e)
        {
            logger.error("返回浏览器结果失败", e);
        }
        finally
        {
            if (pw != null)
            {
                pw.close();
            }
        }
    }

    public static void response( Object o) throws Exception{
        HttpServletResponse response = RequestFilter.getResponse();
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out=response.getWriter();
        out.println(o.toString());
        out.flush();
        out.close();
    }
}
