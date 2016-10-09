package com.myframework.esb.vo;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class SwapVO
{
	private Long tenantId;
	private Long appId;
	private String dataId;
	private String dataType;
	private String dataFormat;
	private String version;
	private Map<String, Object> data;

	@Override
	public String toString()
	{
		JSONObject json = new JSONObject();
		json.put("_app_id_", appId);
		json.put("_tenant_id_", tenantId);
		json.put("_data_type_", dataType);
		json.put("_data_id_", dataId);
		json.put("_data_version_", version);
		json.put("_data_format_", dataFormat);
		json.put("data", JSONObject.toJSON(data));
		return json.toString();
	}

	public Long getTenantId()
	{
		return tenantId;
	}

	public void setTenantId(Long tenantId)
	{
		this.tenantId = tenantId;
	}

	public Long getAppId()
	{
		return appId;
	}

	public void setAppId(Long appId)
	{
		this.appId = appId;
	}

	public String getDataId()
	{
		return dataId;
	}

	public void setDataId(String dataId)
	{
		this.dataId = dataId;
	}

	public String getDataType()
	{
		return dataType;
	}

	public void setDataType(String dataType)
	{
		this.dataType = dataType;
	}

	public Map<String, Object> getData()
	{
		return data;
	}

	public void setData(Map<String, Object> data)
	{
		this.data = data;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getDataFormat()
	{
		return dataFormat;
	}

	public void setDataFormat(String dataFormat)
	{
		this.dataFormat = dataFormat;
	}
}
