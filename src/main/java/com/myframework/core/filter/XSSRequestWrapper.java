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
            return htmlFilter.filter(value);
        }
        return value;
    }
}
