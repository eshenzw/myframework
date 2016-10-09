package com.myframework.extend;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zw on 15/10/21.
 */
public class LruHashMap<K, V> extends LinkedHashMap<K, V> {

    /** 最大数据存储容量 */
    private static final int  LRU_MAX_CAPACITY     = 1024;

    /** 存储数据容量  */
    private int               capacity;

    /**
     * 默认构造方法
     */
    public LruHashMap() {
        super();
    }

    /**
     * 带参数构造方法
     * @param initialCapacity   容量
     * @param loadFactor        装载因子
     * @param isLRU             是否使用lru算法，true：使用（按方案顺序排序）;false：不使用（按存储顺序排序）
     */
    public LruHashMap(int initialCapacity, float loadFactor, boolean isLRU) {
        super(initialCapacity, loadFactor, isLRU);
        capacity = LRU_MAX_CAPACITY;
    }

    /**
     * 带参数构造方法
     * @param initialCapacity   容量
     * @param loadFactor        装载因子
     * @param isLRU             是否使用lru算法，true：使用（按方案顺序排序）;false：不使用（按存储顺序排序）
     * @param lruCapacity       lru存储数据容量
     */
    public LruHashMap(int initialCapacity, float loadFactor, boolean isLRU, int lruCapacity) {
        super(initialCapacity, loadFactor, isLRU);
        this.capacity = lruCapacity;
    }

    /**
     * @see LinkedHashMap#removeEldestEntry(Map.Entry)
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        if(size() > capacity) {
            return true;
        }

        return false;
    }
}
