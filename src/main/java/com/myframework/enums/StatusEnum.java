package com.myframework.enums;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 状态的枚举类
 * @author zw<br />
 * @date 2014-02-17
 */
public enum StatusEnum {

	/**
	 * 正常
	 */
	A("正常"),
	
	/**
	 * 非正常
	 */
	U("非正常");
	
	private StatusEnum(String text) {
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
	
	/**
	 * 获取sql中查询的decode语句
	 * @param fieldName 字段名
	 * @return 返回decode的sql
	 */
	public static String toDecodeSql(String fieldName) {
		StringBuffer sb = new StringBuffer("DECODE(");
		sb.append(fieldName).append(",");
		for (int i = 0; i < values().length; i++) {
			sb.append("'").append(values()[i].toString()).append("','").append(values()[i].getText()).append("',");
		}
		sb.append("'')");
		return sb.toString();
	}
}

