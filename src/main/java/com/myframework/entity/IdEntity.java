package com.myframework.entity;

import java.io.Serializable;

/**
 * 实体编号
 * 
 * @author fengjun
 */
public interface IdEntity extends Serializable
{
	/**
	 * 
	 * @return long
	 */
	Long getId();

	/**
	 * 
	 * @param id
	 *            id
	 */
	void setId(Long id);
}
