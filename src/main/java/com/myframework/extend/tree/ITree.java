package com.myframework.extend.tree;

import com.myframework.entity.IdEntity;

/**
 * treeTable 实体Bean继承接口
 * 
 * @DateTime: 2011-3-4
 * @author author
 * @version 1.0
 */
public interface ITree extends IdEntity
{
	/**
	 * 获得父ID
	 * 
	 * @return Long
	 */
	Long getParentId();
}
