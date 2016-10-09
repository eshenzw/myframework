package com.myframework.threadpool;

import org.springframework.core.task.TaskExecutor;

public class ThreadPoolFactory
{
	public static TaskExecutor getThreadPool()
	{
		return taskExecutor;
	}

	/**
	 * 线程池对象
	 */
	private static TaskExecutor taskExecutor;

	/**
	 * @param taskExecutor
	 *            the taskExecutor to set
	 */
	public void setTaskExecutor(TaskExecutor taskExecutor)
	{
		ThreadPoolFactory.taskExecutor = taskExecutor;
	}

}
