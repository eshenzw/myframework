package com.myframework.core.common.lock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 目的是实现真正意义上的细粒度锁。每个哈希值不同的对象都能获得自己独立的锁。
 * 在测试中，在被锁住的代码执行速度飞快的情况下，效率比分段锁慢 30% 左右。如果有长耗时操作，感觉表现应该会更好
 * @param <T>
 */
public class HashLock<T> {
	private boolean isFair = false;
	private final SegmentLock<T> segmentLock = new SegmentLock<>();// 分段锁
	private final ConcurrentHashMap<T, LockInfo> lockMap = new ConcurrentHashMap<>();

	public HashLock() {
	}

	public HashLock(boolean fair) {
		isFair = fair;
	}

	public void lock(T key) {
		LockInfo lockInfo;
		segmentLock.lock(key);
		try {
			lockInfo = lockMap.get(key);
			if (lockInfo == null) {
				lockInfo = new LockInfo(isFair);
				lockMap.put(key, lockInfo);
			} else {
				lockInfo.count.incrementAndGet();
			}
		} finally {
			segmentLock.unlock(key);
		}
		lockInfo.lock.lock();
	}

	public void unlock(T key) {
		LockInfo lockInfo = lockMap.get(key);
		if (lockInfo.count.get() == 1) {
			segmentLock.lock(key);
			try {
				if (lockInfo.count.get() == 1) {
					lockMap.remove(key);
				}
			} finally {
				segmentLock.unlock(key);
			}
		}
		lockInfo.count.decrementAndGet();
		lockInfo.unlock();
	}

	private static class LockInfo {
		public ReentrantLock lock;
		public AtomicInteger count = new AtomicInteger(1);

		private LockInfo(boolean fair) {
			this.lock = new ReentrantLock(fair);
		}

		public void lock() {
			this.lock.lock();
		}

		public void unlock() {
			this.lock.unlock();
		}
	}
}