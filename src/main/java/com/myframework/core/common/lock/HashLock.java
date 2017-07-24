package com.myframework.core.common.lock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

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