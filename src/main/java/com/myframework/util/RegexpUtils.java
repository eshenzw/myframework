package com.myframework.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexpUtils {
    public static final String REGEXP_TEXT = "^[^\'\"&\\\\]+$";
    public static final String REGEXP_NUMBER = "^\\d*\\.?\\d+$";
    public static final String REGEXP_IDCARD = "^[1-9]([0-9]{14}|[0-9]{16}(\\d|X))$";
    public static final String REGEXP_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
    public static final String REGEXP_MOBILE = "\\d{11}$";
    public static final String REGEXP_TEL = "^(\\d{3,4})?(-)?(\\d{0,8})$";
    public static final String REGEXP_IPPORT = "^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9]):\\d{0,5}$";

    public static boolean matches(String regexp, String value) {
        if (StringUtil.isEmpty(value)) {
            return true;
        } else {
            Pattern pattern = Pattern.compile(regexp);
            Matcher matcher = pattern.matcher(value);
            return matcher.matches();
        }

    }

    public static boolean validateText(String value) {
        return matches(REGEXP_TEXT, value);
    }

    public static boolean validateNumber(String value) {
        return matches(REGEXP_NUMBER, value);
    }

    public static boolean validateIdCard(String value) {
        return matches(REGEXP_IDCARD, value);
    }

    public static boolean validateEmail(String value) {
        return matches(REGEXP_EMAIL, value);
    }

    public static boolean validateMobile(String value) {
        return matches(REGEXP_MOBILE, value);
    }

    public static boolean validateTel(String value) {
        return matches(REGEXP_TEL, value);
    }

    public static boolean validateIpport(String value) {
        return matches(REGEXP_IPPORT, value);
    }

    public static void main(String[] args) {
        System.out.println(validateNumber("222.22"));
    }
}
