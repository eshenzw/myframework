/*
 * 作者：zw
 * 
 */

package com.myframework.extend;

import java.util.Vector;

import org.apache.log4j.Logger;

import com.myframework.util.StringUtil;

public abstract class BaseBuffer<T> implements Runnable {

	private Logger log = Logger.getLogger(this.getClass());

	protected volatile boolean pause;
	protected Vector<T> buf = null;
	protected int threadCount = 1; // 默认一个线程
	protected boolean daemon = true; // 默认为守护线程
	protected String threadName; // 线程名称
	protected int sleepTime = 0; // 睡眠时间

	public BaseBuffer() {
		
	}

	public BaseBuffer(int threadCount) {
		this.setThreadCount(threadCount);
	}

	public BaseBuffer(int threadCount, String threadName) {
		this.setThreadCount(threadCount);
		this.setThreadName(threadName);
	}

	public BaseBuffer(int threadCount, boolean daemon, String threadName) {
		this.setThreadCount(threadCount);
		this.setThreadName(threadName);
		this.setDaemon(daemon);
	}

	public BaseBuffer(int threadCount, boolean daemon, String threadName, int sleepTime) {
		this.setThreadCount(threadCount);
		this.setThreadName(threadName);
		this.setDaemon(daemon);
		this.setSleepTime(sleepTime);
	}

	protected void init() {
		this.pause = false;
		this.buf = new Vector<T>();
		Thread thread = null;
		for (int i = 0; i < this.threadCount; i++) {
			thread = new Thread(this);
			thread.setDaemon(this.daemon);
			if (!StringUtil.isEmpty(this.threadName)) {
				thread.setName(this.threadName + i);
			}
			thread.start();
		}
	}

	public void run() {
		while (true) {
			while (this.buf.size() > 0) {
				try {
					T o = null;
					synchronized (this.buf) {
						o = this.buf.remove(0);
					}
					this.process(o);
					if (this.sleepTime > 0) {
						Thread.sleep(this.sleepTime);
					}
				} catch (Exception ex) {
					this.log.error("", ex);
				}
			}
			this.pause = true;
			if (this.pause) {
				synchronized (this) {
					while (this.pause) {
						try {
							wait();
						} catch (InterruptedException ex) {
							this.log.error("", ex);
						}
					}
				}
			}
		}
	}

	public synchronized void queue(T t) {
		this.buf.add(t);
		if (this.pause) {
			this.pause = false;
			notifyAll();
		}
	}

	/**
	 * 执行线程
	 * 
	 * @param t
	 * @throws Exception
	 */
	public abstract void process(T t);

	public int getBufferCount() {
		return this.buf.size();
	}

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public int getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}

	public boolean isDaemon() {
		return daemon;
	}

	public void setDaemon(boolean daemon) {
		this.daemon = daemon;
	}

}