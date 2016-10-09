package com.myframework.extend;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/*
 * A memory cache implementation which uses a LRU policy.
 * <p>
 * This implementation is thread safe.
 *
 * @author Shintaro Katafuchi
 */
public class LruCache<K, V> {

    /**
     * The flag represents remove all entries in the cache.
     */
    private static final int REMOVE_ALL = -1;

    private static final int DEFAULT_CAPACITY = 10;

    private final Map<K, V> map;

    private final int maxMemorySize;

    private int memorySize;

    public LruCache() {
        this(DEFAULT_CAPACITY);
    }

    public LruCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity <= 0");
        }
        this.map = new LruHashMap<K, V>((int) Math.ceil(capacity / 0.75f) + 1,0.75f, true,capacity);
        maxMemorySize = capacity * 1024 * 1024;
    }

    public final boolean has(K key){
        Objects.requireNonNull(key, "key == null");
        synchronized (this) {
            if(map.containsKey(key)){
                return true;
            }
        }
        return false;
    }

    public final V get(K key) {
        Objects.requireNonNull(key, "key == null");
        synchronized (this) {
            V value = map.get(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    public final V put(K key, V value) {
        Objects.requireNonNull(key, "key == null");
        Objects.requireNonNull(value, "value == null");
        V previous;
        synchronized (this) {
            previous = map.put(key, value);
            memorySize += getValueSize(value);
            if (previous != null) {
                memorySize -= getValueSize(previous);
            }
            trimToSize(maxMemorySize);
        }
        return previous;
    }

    public final V remove(K key) {
        Objects.requireNonNull(key, "key == null");
        V previous;
        synchronized (this) {
            previous = map.remove(key);
            if (previous != null) {
                memorySize -= getValueSize(previous);
            }
        }
        return previous;
    }

    public synchronized final void clear() {
        trimToSize(REMOVE_ALL);
    }

    
    public synchronized final int getMaxMemorySize() {
        return maxMemorySize;
    }

    public synchronized final int getMemorySize() {
        return memorySize;
    }

    /**
     * Returns a copy of the current contents of the cache.
     */
    public synchronized final Map<K, V> snapshot() {
        return new LinkedHashMap<K, V>(map);
    }

    /**
     * Returns the class name.
     * <p>
     * This method should be overridden to debug exactly.
     *
     * @return class name.
     */
    protected String getClassName() {
        return LruCache.class.getName();
    }

    /**
     * Returns the size of the entry.
     * <p>
     * The default implementation returns 1 so that max size is the maximum number of entries.
     * <p>
     * <em>Note:</em> This method should be overridden if you control memory size correctly.
     *
     * @param value value
     * @return the size of the entry.
     */
    protected int getValueSize(V value) {
        return 1;
    }

    /**
     * Remove the eldest entries.
     * <p>
     * <em>Note:</em> This method has to be called in synchronized block.
     *
     * @param maxSize max size
     */
    private void trimToSize(int maxSize) {
        while (true) {
            if (memorySize <= maxSize || map.isEmpty()) {
                break;
            }
            if (memorySize < 0 || (map.isEmpty() && memorySize != 0)) {
                throw new IllegalStateException(getClassName() + ".getValueSize() is reporting inconsistent results");
            }
            Map.Entry<K, V> toRemove = map.entrySet().iterator().next();
            map.remove(toRemove.getKey());
            memorySize -= getValueSize(toRemove.getValue());
        }
    }

    public synchronized final String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            sb.append(entry.getKey())
                    .append('=')
                    .append(entry.getValue())
                    .append(",");
        }
        sb.append("maxMemory=")
                .append(maxMemorySize)
                .append(",")
                .append("memorySize=")
                .append(memorySize);
        return sb.toString();
    }

}
