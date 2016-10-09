package com.myframework.extend.tree;

import java.util.List;

/**
 * treeTable 树集合生成接口
 * 
 * @DateTime: 2011-3-4
 * @author author
 * @version 1.0
 * @param <T>
 */
public interface ITreeManager<T extends ITree>
{
	/**
	 * 方法名称：getTreeList 用途说明: 根据节点ID生成所有子孙节点的有序集合
	 * 
	 * @param list
	 *            list
	 * @param id
	 *            id
	 * @return 子孙的有序List
	 */
	List<T> getTreeList(List<T> list, Long id);

}
