package com.myframework.core.filter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class XSSRequestWrapper extends HttpServletRequestWrapper {
    public XSSRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = stripXSS(values[i]);
        }
        return encodedValues;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        return stripXSS(value);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return stripXSS(value);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        HashMap<String, String[]> paramMap = (HashMap<String, String[]>) super.getParameterMap();

        for (Iterator<Entry<String, String[]>> iterator = paramMap.entrySet().iterator(); iterator.hasNext(); ) {
            Entry<String, String[]> entry = iterator.next();
            String[] values = entry.getValue();
            for (int i = 0; i < values.length; i++) {
                values[i] = stripXSS(values[i]);
            }
            entry.setValue(values);
        }
        return paramMap;

    }

    private String stripXSS(String value) {
        if (value != null) {
            Pattern scriptPattern = Pattern.compile("<script", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("&lt;script");
            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("&lt;/script&gt;");
            //
            scriptPattern = Pattern.compile("<iframe", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("&lt;iframe");
            scriptPattern = Pattern.compile("</iframe>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("&lt;/iframe&gt;");
            //
            scriptPattern = Pattern.compile("<img", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("&lt;img");
            scriptPattern = Pattern.compile("</img>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("&lt;/img&gt;");
            //
            scriptPattern = Pattern.compile("<image", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("&lt;image");
            scriptPattern = Pattern.compile("</image>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("&lt;/image&gt;");
            //
            scriptPattern = Pattern.compile("<style", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("&lt;style");
            scriptPattern = Pattern.compile("</style>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("&lt;/style&gt;");
            //
            scriptPattern = Pattern.compile("<vbscript", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("&lt;vbscript");
            scriptPattern = Pattern.compile("</vbscript>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("&lt;/vbscript&gt;");
            //
            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
                    | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            // // NOTE: It's highly recommended to use the ESAPI library and
            // // uncomment the following line to
            // // avoid encoded attacks.
            // // value = ESAPI.encoder().canonicalize(value);
            // // Avoid null characters
            // // value = value.replaceAll("", "");
            // // Avoid anything between script tags
            // Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>",
            // Pattern.CASE_INSENSITIVE);
            // value = scriptPattern.matcher(value).replaceAll("");
            // // Avoid anything in a
            // // src="http://www.yihaomen.com/article/java/..." type of
            // // e­xpression
            // scriptPattern =
            // Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",
            // Pattern.CASE_INSENSITIVE
            // | Pattern.MULTILINE | Pattern.DOTALL);
            // value = scriptPattern.matcher(value).replaceAll("");
            // scriptPattern =
            // Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",
            // Pattern.CASE_INSENSITIVE
            // | Pattern.MULTILINE | Pattern.DOTALL);
            // value = scriptPattern.matcher(value).replaceAll("");
            // // Remove any lonesome </script> tag
            // scriptPattern = Pattern.compile("</script>",
            // Pattern.CASE_INSENSITIVE);
            // value = scriptPattern.matcher(value).replaceAll("");
            // // Remove any lonesome <script ...> tag
            // scriptPattern = Pattern.compile("<script(.*?)>",
            // Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
            // | Pattern.DOTALL);
            // value = scriptPattern.matcher(value).replaceAll("");
            // // Avoid eval(...) e­xpressions
            // scriptPattern = Pattern.compile("eval\\((.*?)\\)",
            // Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
            // | Pattern.DOTALL);
            // value = scriptPattern.matcher(value).replaceAll("");
            // // Avoid e­xpression(...) e­xpressions
            // scriptPattern = Pattern.compile("e­xpression\\((.*?)\\)",
            // Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
            // | Pattern.DOTALL);
            // value = scriptPattern.matcher(value).replaceAll("");
            // // Avoid javascript:... e­xpressions
            // scriptPattern = Pattern.compile("javascript:",
            // Pattern.CASE_INSENSITIVE);
            // value = scriptPattern.matcher(value).replaceAll("");
            // // Avoid vbscript:... e­xpressions
            // scriptPattern = Pattern.compile("vbscript:",
            // Pattern.CASE_INSENSITIVE);
            // value = scriptPattern.matcher(value).replaceAll("");
            // // Avoid onload= e­xpressions
            // scriptPattern = Pattern.compile("onload(.*?)=",
            // Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
            // | Pattern.DOTALL);
            // value = scriptPattern.matcher(value).replaceAll("");
        }
        return value;
    }
}
