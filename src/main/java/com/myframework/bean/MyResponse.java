package com.myframework.bean;

import com.myframework.core.error.exception.BizException;
import com.myframework.pagehelper.PageInfo;
import net.sf.json.JSONObject;

import java.util.HashMap;

/**
 * Created by zw on 2017/8/15.
 */
public class MyResponse extends HashMap<String, Object> {

    /**
     * 默认错误码
     */
    public static final int CODE_FAIL_DEFAULT = 0;
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_UNKNOWN = 500;

    private MyResponse(int code, Object data, String message) {
        this.put("code", code);
        this.put("success", CODE_SUCCESS == code);
        this.put("message", message);
        this.put("data", data);
    }

    @Override
    public Object put(String key, Object value) {
        return super.put(key, value == null ? "" : value);
    }

    public String getMessage() {
        return this.get("message") == null ? "" : (String) this.get("message");
    }

    public Object getData() {
        return this.get("data") == null ? "" : this.get("data");
    }

    public boolean isSuccess() {
        return CODE_SUCCESS == this.get("code");
    }

    public String getCode() {
        return (String) this.get("code");
    }

    public static final MyResponse success() {
        return new MyResponse(CODE_SUCCESS, null, null);
    }

    public static final MyResponse success(Object data) {
        return new MyResponse(CODE_SUCCESS, data, null);
    }

    public static final MyResponse success(Object data, String message) {
        return new MyResponse(CODE_SUCCESS, data, message);
    }

    public static final MyResponse pageInfo(PageInfo pageInfo) {
        MyResponse api = MyResponse.success();
        api.putAll(JSONObject.fromObject(pageInfo));
        return api;
    }

    public static final MyResponse failed(int code, String message) {
        return new MyResponse(code, null, message);
    }

    public static final MyResponse failed(String message) {
        return new MyResponse(CODE_FAIL_DEFAULT, null, message);
    }


    public static final MyResponse failed(BizException e) {
        return new MyResponse(e.getErrorCode(), null, e.getMessage());
    }

    public static final MyResponse failed(Exception e, String message) {
        return new MyResponse(CODE_UNKNOWN, null, message);
    }

}
