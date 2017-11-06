package com.myframework.extend.JsonDeserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.myframework.exception.RtException;
import com.myframework.util.StringUtil;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zw on 2017/10/7.
 */
public class CustomJsonDateTimeDeserializer extends JsonDeserializer {
    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = jp.getText();
        if (StringUtil.isNullOrEmpty(date)) {
            return null;
        }
        try {
            return format.parse(date);
        } catch (ParseException e) {
            throw new RtException("格式化日期时间格式出错！");
        }
    }
}
