package com.myframework.scheduler;

/**
 * 调度接口
 * 
 * @author zw
 * 
 */
public interface SchedulerService
{
	/**
	 * 操作任务
	 * 
	 * @param name
	 *            触发器名
	 * @param cronExpression
	 *            调度规则
	 * @param flag
	 *            标志位
	 */
	void schedule(String name, String cronExpression, String flag);

	/**
	 * 新增
	 * 
	 * @param name
	 * @param cronExpression
	 */
	void promptlyScheduler();

	/**
	 * 开始
	 * 
	 * @param triggerName
	 *            触发器名
	 * @param group
	 *            组名
	 */
	void startTrigger(String triggerName, String group);

	/**
	 * 暂停
	 * 
	 * @param triggerName
	 *            触发器名
	 * @param group
	 *            组名
	 */
	void pauseTrigger(String triggerName, String group);

	/**
	 * 恢复
	 * 
	 * @param triggerName
	 *            触发器名
	 * @param group
	 *            组名
	 */
	void resumeTrigger(String triggerName, String group);

	/**
	 * 移除
	 * 
	 * @param triggerName
	 *            触发器名 *
	 * @return 标志位
	 */
	boolean removeTrigdger(String triggerName);

}
