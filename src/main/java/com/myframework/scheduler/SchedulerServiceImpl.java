package com.myframework.scheduler;

import java.text.ParseException;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.quartz.*;

/**
 * 调度实现类
 * 
 * @author zw
 * 
 */
public class SchedulerServiceImpl implements SchedulerService
{
	/**
	 * 日志信息
	 */
	private static final Logger LOGGER = Logger.getLogger(SchedulerServiceImpl.class);
	/**
	 * 调度
	 */
	private Scheduler scheduler;

	/**
	 * 调度Job
	 */
	private JobDetail jobDetail;
	/**
	 * 立即调度Job
	 */
	private JobDetail backupPromptlyJobDetail;

	/**
	 * 初始化任务
	 */
	public void initSchedule()
	{
		schedule("databackUpTrigger", "00 00 00 * * ?", "init");
	}

	@Override
	public void schedule(String name, String cronExpression, String flag)
	{
		if (name == null || "".equals(name.trim()))
		{
			name = UUID.randomUUID().toString();
		}

		try
		{
			scheduler.addJob(jobDetail, true);
			CronTrigger cronTrigger = new CronTrigger(name, scheduler.DEFAULT_GROUP, jobDetail.getName(),
					scheduler.DEFAULT_GROUP);
			try
			{
				cronTrigger.setCronExpression(new CronExpression(cronExpression));
				cronTrigger.setDescription("数据备份");
			}
			catch (ParseException e)
			{
				LOGGER.error(e.getMessage(), e);
			}

			Trigger t = scheduler.getTrigger(name, scheduler.DEFAULT_GROUP);
			if (t == null)
			{
				scheduler.scheduleJob(cronTrigger);
			}
			else if (!"init".equals(flag))
			{
				scheduler.rescheduleJob(name, scheduler.DEFAULT_GROUP, cronTrigger);
			}

		}
		catch (SchedulerException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * 操作任务
	 * 
	 * @param name
	 *            触发器名称
	 */
	public void schedule2(String name)
	{
		if (name == null || "".equals(name.trim()))
		{
			name = UUID.randomUUID().toString();
		}

		try
		{

			Trigger t = scheduler.getTrigger(name, scheduler.DEFAULT_GROUP);

			scheduler.rescheduleJob(name, scheduler.DEFAULT_GROUP, t);

		}
		catch (SchedulerException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void promptlyScheduler()
	{

		try
		{
			Trigger trigger = TriggerUtils.makeImmediateTrigger(0, 0);
			trigger.setName(UUID.randomUUID().toString());
			backupPromptlyJobDetail.setName(UUID.randomUUID().toString());
			scheduler.scheduleJob(backupPromptlyJobDetail, trigger);
		}
		catch (SchedulerException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void startTrigger(String triggerName, String group)
	{
		try
		{
			scheduler.resumeTrigger(triggerName, group);
		}
		catch (SchedulerException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void pauseTrigger(String triggerName, String group)
	{
		try
		{
			scheduler.pauseTrigger(triggerName, group);
		}
		catch (SchedulerException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void resumeTrigger(String triggerName, String group)
	{
		try
		{

			scheduler.resumeTrigger(triggerName, group);
		}
		catch (SchedulerException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean removeTrigdger(String triggerName)
	{
		try
		{
			Trigger t = scheduler.getTrigger(triggerName, scheduler.DEFAULT_GROUP);
			if (t != null)
			{
				scheduler.pauseTrigger(triggerName, scheduler.DEFAULT_GROUP);
				return scheduler.unscheduleJob(triggerName, scheduler.DEFAULT_GROUP);
			}
			return false; // todo return不应该写死
		}
		catch (SchedulerException e)
		{
			throw new RuntimeException(e);
		}
	}

	public Scheduler getScheduler()
	{
		return scheduler;
	}

	public JobDetail getJobDetail()
	{
		return jobDetail;
	}

	public void setScheduler(Scheduler scheduler)
	{
		this.scheduler = scheduler;
	}

	public void setJobDetail(JobDetail jobDetail)
	{
		this.jobDetail = jobDetail;
	}

	public JobDetail getBackupPromptlyJobDetail()
	{
		return backupPromptlyJobDetail;
	}

	public void setBackupPromptlyJobDetail(JobDetail backupPromptlyJobDetail)
	{
		this.backupPromptlyJobDetail = backupPromptlyJobDetail;
	}

}
