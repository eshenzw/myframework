package com.myframework.extend.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * TreeTable 方法具体实现
 * 
 * @DateTime: 2011-3-4
 * @author author
 * @version 1.0
 * @param <T>
 */
public class DefaultTreeManager<T extends ITree> implements ITreeManager<T>
{
	/***/
	private HashMap<Long, List<T>> pidMap;

	@Override
	public List<T> getTreeList(List<T> list, Long pid)
	{
		pidMap = initParentIdMap(list);
		List<T> treeList = new ArrayList<T>();
		sortTree(treeList, pid);
		return treeList;
	}

	/**
	 * 排序子节点
	 * 
	 * @param treeList
	 *            treeList
	 * @param pid
	 *            pid
	 */
	private void sortTree(List<T> treeList, Long pid)
	{
		List<T> childList = pidMap.get(pid);
		if (childList == null)
		{
			return;
		}
		for (T tree : childList)
		{
			treeList.add(tree);
			sortTree(treeList, tree.getId());
		}
	}

	/**
	 * 初始化parentID到children的List映射
	 * 
	 * @param list
	 *            list
	 * @return hashmap
	 */
	public HashMap<Long, List<T>> initParentIdMap(List<T> list)
	{
		HashMap<Long, List<T>> pidMap = new HashMap<Long, List<T>>();
		for (T tree : list)
		{
			Long pid = tree.getParentId();
			List<T> childList = pidMap.get(pid);
			if (childList == null)
			{
				childList = new ArrayList<T>();
			}
			childList.add(tree);
			pidMap.put(pid, childList);
		}
		return pidMap;
	}
}
