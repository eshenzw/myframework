package com.myframework.autocode.entity;

/**
 * 类属性信息
 */
public class PropertyInfo
{

	/*
	 * 属性名称
	 */
	private String propertyName;

	/*
	 * 属性类型
	 */
	private String propertyType;

	/*
	 * 方法名称（首字母大写）
	 */
	private String methodName;

	public PropertyInfo()
	{

	}

	public PropertyInfo(ColumnInfo columnInfo)
	{
		// 转换数据列名为JAVA属性名称
		this.propertyName = convertColumnName(columnInfo.getName());
		// 转换数据列类型为JAVA变量类型
		this.propertyType = convertColumnType(columnInfo.getType(), columnInfo.getLength());

		// 大写属性名称的第一个字符作为方法名
		char[] propertyChars = this.propertyName.toCharArray();
		propertyChars[0] = Character.toUpperCase(propertyChars[0]);
		this.methodName = String.valueOf(propertyChars);
	}

	@Override
	public String toString()
	{
		return "PropertyInfo [propertyName=" + propertyName + ", propertyType=" + propertyType + ", methodName="
				+ methodName + "]";
	}

	/*
	 * 转换数据列名为JAVA属性名称
	 */
	private String convertColumnName(String columnName)
	{
		String[] columnNameWords = columnName.toLowerCase().split("_");
		String propertyName = columnNameWords[0];
		if (columnNameWords.length > 1)
		{
			for (int i = 1; i < columnNameWords.length; i++)
			{
				String columnNameWord = columnNameWords[i];
				char[] chars = columnNameWord.toCharArray();
				chars[0] = Character.toUpperCase(chars[0]);
				propertyName += String.valueOf(chars);
			}
		}
		return propertyName;
	}

	/*
	 * 转换数据列类型为JAVA变量类型
	 * 
	 * @Tinyint int Smallint int Mediumint long Int int Bigint long Float float
	 * Decimal BigDecimal Char String Varchar String Text String Date Date Time
	 * Date Timestamp Date
	 */
	private String convertColumnType(String columnType, int length)
	{
		String propertyType = "";
		if (columnType.toLowerCase().contains("tinyint"))
		{
			propertyType = "int";
		}
		else if (columnType.toLowerCase().contains("smallint"))
		{
			propertyType = "int";
		}
		else if (columnType.toLowerCase().contains("mediumint"))
		{
			propertyType = "int";
		}
		else if (columnType.toLowerCase().contains("bigint"))
		{
			propertyType = "Long";
		}
		else if (columnType.toLowerCase().contains("int"))
		{
			if(length<=11){
				propertyType = "int";
			}else{
				propertyType = "Long";
			}
		}
		else if (columnType.toLowerCase().contains("float"))
		{
			propertyType = "Float";
		}
		else if (columnType.toLowerCase().contains("decimal"))
		{
			propertyType = "BigDecimal";
		}
		else if (columnType.toLowerCase().contains("char"))
		{
			propertyType = "String";
		}
		else if (columnType.toLowerCase().contains("varchar"))
		{
			propertyType = "String";
		}
		else if (columnType.toLowerCase().contains("text"))
		{
			propertyType = "String";
		}
		else if (columnType.toLowerCase().contains("date"))
		{
			propertyType = "Date";
		}
		else if (columnType.toLowerCase().contains("timestamp"))
		{
			propertyType = "Date";
		}
		else if (columnType.toLowerCase().contains("time"))
		{
			propertyType = "Date";
		}
		else
		{
			propertyType = "Object";
		}
		return propertyType;
	}

	public String getPropertyName()
	{
		return propertyName;
	}

	public void setPropertyName(String propertyName)
	{
		this.propertyName = propertyName;
	}

	public String getPropertyType()
	{
		return propertyType;
	}

	public void setPropertyType(String propertyType)
	{
		this.propertyType = propertyType;
	}

	public String getMethodName()
	{
		return methodName;
	}

	public void setMethodName(String methodName)
	{
		this.methodName = methodName;
	}

}
