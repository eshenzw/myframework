package com.myframework.enums;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum UserTypeEnum {
	Normal("正常用户"),
	Admin("后台用户"),
	Business("商家用户")
	;
	
	private UserTypeEnum(String text) {
		this.text = text;
	}
	private String text;

	public String getText() {
		return text;
	}
	
	
	/**
	 * 返回json格式的数据
	 * @return 返回json格式的数据
	 */
	public static JSONArray toJson() {
		JSONArray arr = new JSONArray();
		JSONObject json = null;
		for (int i = 0; i < values().length; i++) {
			json = new JSONObject();
			json.put("text", values()[i].getText());
			json.put("value", values()[i].toString());
			arr.add(json);
		}
		return arr;
	}
}
