package com.myframework.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.myframework.constant.Constants;
import com.myframework.core.entity.KeyValueEntity;

/**
 * @DateTime: 2011-3-4
 * @author author
 * @version 1.0
 */
public final class JsonUtil
{
	public static final int ERR_NONE = 0;
	public static final int ERR_CODE_DEFAULT = 1;

	public static final String DATE_FORMAT_SHORT = "yyyy-MM-dd";
	public static final String DATE_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 私有构造函数
	 */
	private JsonUtil()
	{

	}

	/**
	 * data=[{"id":"1"},{"id":2}]用json里的数据，创建map.
	 * 
	 * @param json
	 *            json字符串
	 * @return Map
	 */
	public static Map<String, String> json2Map(String json)
	{
		Map<String, String> map = new LinkedHashMap<String, String>();
		if (json != null && json.length() > 0)
		{
			try
			{
				// 用于解析json数组
				if (json.startsWith("[") && json.endsWith("]"))
				{
					JSONArray jsonArray = JSONArray.fromObject(json);
					for (int i = 0; i < jsonArray.size(); i++)
					{
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						for (Object o : jsonObject.keySet())
						{
							map.put(String.valueOf(o), String.valueOf(jsonObject.get(o)));
						}
					}
				}
				else
				{
					// 解析json对象
					JSONObject jsonObject = JSONObject.fromObject(json);
					for (Object o : jsonObject.keySet())
					{
						map.put(String.valueOf(o), String.valueOf(jsonObject.get(o)));
					}
				}
			}
			catch (RuntimeException e)
			{
				map.clear();
				throw new RuntimeException("json字符串解析失败!", e);
			}
		}
		return map;
	}

	/**
	 * List2 json.
	 * 
	 * @param kvList
	 *            the kv list
	 * @return the string
	 */
	public static String list2Json(List<KeyValueEntity> kvList)
	{
		if (kvList != null)
		{
			JSONArray jsonArray = new JSONArray();
			for (KeyValueEntity kv : kvList)
			{
				if (kv != null)
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put(kv.getSaveValue(), kv.getDisplayValue());
					jsonArray.add(jsonObject);
				}
			}
			return jsonArray.toString();
		}
		else
		{
			return "";
		}
	}

	/**
	 * Json2 list.
	 * 
	 * @param json
	 *            the json
	 * @return the list< key value entity>
	 */
	public static List<KeyValueEntity> json2List(String json)
	{
		if (StringUtil.isNotEmpty(json))
		{
			List<KeyValueEntity> kvList = new ArrayList<KeyValueEntity>();
			JSONArray jsonArray = JSONArray.fromObject(json);
			for (int i = 0; i < jsonArray.size(); i++)
			{
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				for (Object o : jsonObject.keySet())
				{
					KeyValueEntity kv = new KeyValueEntity();
					kv.setSaveValue(o.toString());
					kv.setDisplayValue(jsonObject.get(o).toString());
					kvList.add(kv);
				}
			}
			return kvList;
		}
		else
		{
			return null;
		}
	}

	/**
	 * 创建JSON对象信息
	 * 
	 * @param code
	 *            消息编码
	 * @param message
	 *            错误信息
	 * @return 错误提示消息JSON对象字符串
	 */
	public static String jsonMsgStr(int code, String message)
	{
		return jsonMsg(code, message).toString();
	}

	/**
	 * 创建JSON对象信息
	 * 
	 * @param code
	 *            消息编码
	 * @param message
	 *            错误信息
	 * @return 错误提示消息JSON对象
	 */
	public static JSONObject jsonMsg(int code, Object message)
	{
		JSONObject jsonMessage = new JSONObject();
		jsonMessage.put("err", code);
		if(code != 0){
			jsonMessage.put("errMsg", (message == null || message.toString().equals(""))? Constants.ERROR_MSG:message);
		}
		return jsonMessage;
	}

	public static String errMsg(String message)
	{
		return JsonUtil.jsonMsgStr(JsonUtil.ERR_CODE_DEFAULT, message);
	}

	public static String okMsg()
	{
		return JsonUtil.jsonMsgStr(JsonUtil.ERR_NONE, null);
	}

	/**
	 * 创建成功JSON对象 jsonSuccess
	 *
	 * @return 成功消息JSON对象
	 */
	public static String jsonSuccess()
	{
		JSONObject jsonSuccess = new JSONObject();
		jsonSuccess.put("success", true);
		return jsonSuccess.toString();
	}

	/**
	 * 创建错误信息JSON对象 jsonError
	 * 
	 * @param message
	 *            错误信息
	 * @return 错误提示消息JSON对象
	 */
	public static String jsonError(String message)
	{
		JSONObject jsonError = new JSONObject();
		jsonError.put("error", message);
		return jsonError.toString();
	}

	/**
	 * 创建JSON对象 newInstance
	 * 
	 * @return JSONObject
	 */
	public static JSONObject newInstance()
	{
		return new JSONObject();
	}

	/**
	 * 声明JSONARRAY对象
	 * 
	 * @return JSONARRAY
	 */
	public static JSONArray newArray()
	{
		return new JSONArray();
	}

	/**
	 * JSON对象新增子节点 addElement
	 * 
	 * @param node
	 *            节点
	 * @param key
	 *            属性
	 * @param value
	 *            值
	 * @return 节点
	 */
	public static JSONObject addElement(JSONObject node, String key, String value)
	{
		if (node == null)
		{
			node = new JSONObject();
		}
		if (StringUtil.isEmpty(key))
		{
			return node;
		}
		if (StringUtil.isEmpty(value))
		{
			value = "&nbsp;";
		}
		node.put(key, value);
		return node;
	}

	public static final String toJSONString(Object object)
	{
		return toJSONString(object, DATE_FORMAT_FULL);
	}

	public static final String toJSONString(Object object, String dateFormat)
	{
		SerializeWriter out = new SerializeWriter();
		try
		{
			JSONSerializer serializer = new JSONSerializer(out);
			serializer.config(SerializerFeature.DisableCircularReferenceDetect, true);
			// 日期
			serializer.config(SerializerFeature.WriteDateUseDateFormat, true);
			if (dateFormat != null)
			{
				serializer.setDateFormat(dateFormat);
			}
			// 将null显示为空 不丢弃
			serializer.getValueFilters().add(new ValueFilter()
			{
				public Object process(Object obj, String s, Object value)
				{
					return value != null ? value : "";
				}
			});

			serializer.write(object);

			return out.toString();
		}
		finally
		{
			out.close();
		}
	}
}
