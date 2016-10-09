/*
 * 作者：zw
 * 
 */
package com.myframework.extend;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

public abstract class BaseTimer {

	Logger log = Logger.getLogger(this.getClass());

	protected boolean daemon = true; // 守护定时器
	protected long delay; // 服务启动后，延迟多少时间开始（单位：毫秒）
	protected long period; // 周期
	/**
	 * 是否定点执行
	 * 为true时，则每到一个周期执行一次，不计算执行的时间
	 * 为false时，则在执行完后再计算周期时间
	 */
	protected boolean isTimer = true;

	public BaseTimer() {
		
	}

	public BaseTimer(long delay, long period) {
		this.delay = delay;
		this.period = period;
	}

	public BaseTimer(long delay, long period, boolean daemon) {
		this.delay = delay;
		this.period = period;
		this.daemon = daemon;
	}

	protected void init() {
		Timer timer = new Timer(this.daemon);
		if (isTimer) {
			timer.schedule(new AutoStat(), this.delay, this.period);
		} else {
			timer.schedule(new AutoStat(), this.delay, 1);
		}
	}

	class AutoStat extends TimerTask {
		public void run() {
			process();
			if (!isTimer) {
				try {
					Thread.sleep(period);
				} catch (InterruptedException e) {
					log.error("", e);
				}
			}
		}
	}

	/**
	 * 执行定时器
	 */
	public abstract void process();

	public void setDaemon(boolean daemon) {
		this.daemon = daemon;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

	public void setIsTimer(boolean isTimer) {
		this.isTimer = isTimer;
	}

	public boolean getDaemon() {
		return daemon;
	}

	public long getDelay() {
		return delay;
	}

	public long getPeriod() {
		return period;
	}

	public boolean getIsTimer() {
		return isTimer;
	}

}
