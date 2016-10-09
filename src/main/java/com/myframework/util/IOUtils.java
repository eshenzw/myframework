package com.myframework.util;

import java.io.Closeable;
import java.io.IOException;

public class IOUtils
{
	/**
	 * 关闭的数据源或目标
	 * 
	 * @param closeable
	 *            数据源或目标 调用 close 方法可释放对象保存的资源（如打开文件)
	 */
	public static void close(Closeable closeable)
	{
		if (closeable == null)
		{
			return;
		}
		try
		{
			closeable.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
