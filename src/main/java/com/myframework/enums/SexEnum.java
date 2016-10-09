package com.myframework.enums;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 性别枚举
 * @author zw
 *
 */
public enum SexEnum {

	MALE("男性"),
	
	FEMALE("女性");
	
	private SexEnum(String text) {
		this.text = text;
	}

	private String text;

	public String getText() {
		return this.text;
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
