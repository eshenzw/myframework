package com.myframework.autocode.entity;

import com.myframework.autocode.util.CodeGeneratorUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据表列信息
 */
public class ColumnInfo
{

	private static Pattern pattern = Pattern.compile("^([^(]+)(\\((.*)\\))?");

	/*
	 * 元素名称
	 */
	private String name;

	/*
	 * 是否为Key
	 */
	private boolean isKey;

	/*
	 * 是否非空
	 */
	private boolean notNull;

	/*
	 * 类型
	 */
	private String type;

	/*
	 * 长度
	 */
	private int length;

	/*
	 * 描述
	 */
	private String description;

	/*
	 * 示例
	 */
	private String example;

	@Override
	public String toString()
	{
		return "ColumnInfo [name=" + name + ", type=" + type + ", description=" + description + "]";
	}

	public String getDbColumnType(CodeGeneratorUtils.DB_TYPE_ENUM dbtype)
	{
		String colType = "";
		if (dbtype == CodeGeneratorUtils.DB_TYPE_ENUM.MYSQL)
		{
			Matcher matcher = pattern.matcher(this.type);
			if (matcher.matches())
			{
				String dbTypeName = matcher.group(1);
				String dbTypeLength = matcher.group(3);
				colType = dbTypeName.toUpperCase();
				if((dbTypeLength == null || "".equals(dbTypeLength.trim())) && this.getLength() > 0){
					dbTypeLength = String.valueOf(this.getLength());
				}
				colType += dbTypeLength == null ? "" : ("(" + dbTypeLength + ")");
			}
		}
		else if (dbtype == CodeGeneratorUtils.DB_TYPE_ENUM.SQLSERVER)
		{
			Matcher matcher = pattern.matcher(this.type);
			if (matcher.matches())
			{
				String dbTypeName = matcher.group(1);
				String dbTypeLength = matcher.group(3);
				if ("VARCHAR2".equalsIgnoreCase(dbTypeName) || "VARCHAR".equalsIgnoreCase(dbTypeName))
				{
					colType = "VARCHAR";
				}
				else if ("NUMBER".equalsIgnoreCase(dbTypeName))
				{
					colType = "NUMERIC";
				}
				else if ("CHAR".equalsIgnoreCase(dbTypeName))
				{
					colType = "CHAR";
				}
				if((dbTypeLength == null || "".equals(dbTypeLength.trim())) && this.getLength() > 0){
					dbTypeLength = String.valueOf(this.getLength());
				}
				colType += dbTypeLength == null ? "" : ("(" + dbTypeLength + ")");
			}
		}
		else if (dbtype == CodeGeneratorUtils.DB_TYPE_ENUM.ORACLE)
		{
			Matcher matcher = pattern.matcher(this.type);
			if (matcher.matches())
			{
				String dbTypeName = matcher.group(1);
				String dbTypeLength = matcher.group(3);
				if ("VARCHAR2".equalsIgnoreCase(dbTypeName) || "VARCHAR".equalsIgnoreCase(dbTypeName))
				{
					colType = "VARCHAR2";
				}
				else if ("NUMBER".equalsIgnoreCase(dbTypeName))
				{
					colType = "NUMBER";
				}
				else if ("CHAR".equalsIgnoreCase(dbTypeName))
				{
					colType = "CHAR";
				}
				if((dbTypeLength == null || "".equals(dbTypeLength.trim())) && this.getLength() > 0){
					dbTypeLength = String.valueOf(this.getLength());
				}
				colType += dbTypeLength == null ? "" : ("(" + dbTypeLength + ")");
			}
		}
		return colType;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isKey()
	{
		return isKey;
	}

	public void setKey(boolean isKey)
	{
		this.isKey = isKey;
	}

	public boolean isNotNull()
	{
		return notNull;
	}

	public void setNotNull(boolean notNull)
	{
		this.notNull = notNull;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public int getLength()
	{
		return length;
	}

	public void setLength(int length)
	{
		this.length = length;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getExample()
	{
		return example;
	}

	public void setExample(String example)
	{
		this.example = example;
	}

}
