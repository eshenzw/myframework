package com.myframework.core.common.lock;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 分段锁，系统提供一定数量的原始锁，根据传入对象的哈希值获取对应的锁并加锁
 * 注意：要锁的对象的哈希值如果发生改变，有可能导致锁无法成功释放!!!
 */
public class SegmentLock<T> {

	private Integer segments = 16;// 默认分段数量
	private final HashMap<Integer, ReentrantLock> lockMap = new HashMap<>();

	public SegmentLock() {
		init(null, false);
	}

	public SegmentLock(Integer counts, boolean fair) {
		init(counts, fair);
	}

	private void init(Integer counts, boolean fair) {
		if (counts != null) {
			segments = counts;
		}
		for (int i = 0; i < segments; i++) {
			lockMap.put(i, new ReentrantLock(fair));
		}
	}

	/**
	 * lock
	 * 
	 * @param key
	 * @return
	 */
	public boolean lock(T key) {
		ReentrantLock lock = lockMap.get(getAbsHashKey(key));
		return lock.tryLock();
	}

	/**
	 * lock ，如果获取不到，等一会
	 * 
	 * @param key
	 * @param timeout
	 * @param unit
	 * @return
	 */
	public boolean lock(T key, long timeout, TimeUnit unit) {
		ReentrantLock lock = lockMap.get(getAbsHashKey(key));
		try {
			return lock.tryLock() || lock.tryLock(timeout, unit);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 解锁
	 * @param key
	 */
	public void unlock(T key) {
		ReentrantLock lock = lockMap.get(getAbsHashKey(key));
		lock.unlock();
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	private int getAbsHashKey(T key) {
		return Math.abs(key.hashCode() % segments);
	}
}