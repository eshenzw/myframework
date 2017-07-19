package com.myframework.core.entity;

/**
 * The Class ComponentContentEntity.
 * 
 * @author fengjun
 */
public class KeyValueEntity
{
	/** The save value. */
	private String saveValue;
	/** The display value. */
	private String displayValue;
	/**
	 * 查询关键字
	 */
	private String keyword;
	/**
	 * 中文拼音
	 */
	private String chinese;

	public String getKeyword()
    {
        return keyword;
    }

    public void setKeyword(String keyword)
    {
        this.keyword = keyword;
    }

    public String getChinese()
    {
        return chinese;
    }

    public void setChinese(String chinese)
    {
        this.chinese = chinese;
    }

    /**
	 * @return the saveValue
	 */
	public String getSaveValue()
	{
		return saveValue;
	}

	/**
	 * @param saveValue
	 *            the saveValue to set
	 */
	public void setSaveValue(String saveValue)
	{
		this.saveValue = saveValue;
	}

	/**
	 * @return the displayValue
	 */
	public String getDisplayValue()
	{
		return displayValue;
	}

	/**
	 * @param displayValue
	 *            the displayValue to set
	 */
	public void setDisplayValue(String displayValue)
	{
		this.displayValue = displayValue;
	}
}
