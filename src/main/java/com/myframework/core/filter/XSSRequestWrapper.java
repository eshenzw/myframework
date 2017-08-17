package com.myframework.core.filter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class XSSRequestWrapper extends HttpServletRequestWrapper {
    public XSSRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    //html过滤
    private final static HTMLFilter htmlFilter = new HTMLFilter();

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
        Map<String, String[]> paramMap = super.getParameterMap();
        Method method = null;
        Method method1 = null;
        boolean isLocked = false;
        try {
            method = paramMap.getClass().getMethod("setLocked", new Class[]{boolean.class});
            method1 = paramMap.getClass().getMethod("isLocked");
            if (method != null && method1 != null) {
                isLocked = (boolean) method1.invoke(paramMap);
                method.invoke(paramMap, new Object[]{new Boolean(false)});
            }
            for (Iterator<Entry<String, String[]>> iterator = paramMap.entrySet().iterator(); iterator.hasNext(); ) {
                Entry<String, String[]> entry = iterator.next();
                String[] values = entry.getValue();
                for (int i = 0; i < values.length; i++) {
                    values[i] = stripXSS(values[i]);
                }
                //entry.setValue(values);
                paramMap.put(entry.getKey(), values);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            if (method != null && method1 != null) {
                try {
                    method.invoke(paramMap, new Object[]{new Boolean(isLocked)});
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return paramMap;

    }

    private String stripXSS(String value) {
        if (value != null) {
            return htmlFilter.filter(value);
        }
        return value;
    }
}
