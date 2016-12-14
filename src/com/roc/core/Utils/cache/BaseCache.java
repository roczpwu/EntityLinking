package com.roc.core.Utils.cache;

import java.util.*;
import java.util.concurrent.locks.*;

/**
 * User: rocwu
 * Date: 2016/11/17
 * Time: 19:31
 * Desc: 缓存基类
 */
public abstract class BaseCache<K, V> {

    private final int capacity;
    private final Map<K, V> map;
    private final ReadWriteLock readWriteLock;

    public BaseCache(int capacity) {
        this.capacity = capacity;
        map = new HashMap<>();
        readWriteLock = new ReentrantReadWriteLock();
    }

    /**
     * override by subclass
     * @param key key
     * @return value from non-cache (e.g. db)
     */
    protected abstract V getDirectly(K key);

    public final V get(K key) {
        readWriteLock.readLock().lock();
        V value = map.get(key);
        readWriteLock.readLock().unlock();
        if (value == null) {
            value = getDirectly(key);
            if (value != null) set(key, value);
        }
        return value;
    }

    public final V set(K key, V value) {
        readWriteLock.writeLock().lock();
        try {
            if (map.containsKey(key)) {
                map.put(key,value);
            }
            else {
                //TODO: 不命中的策略待完善
                while (map.size() >= capacity)
                    map.remove(map.keySet().iterator().next());
                map.put(key, value);
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
        return value;
    }

    public final V remove(K key) {
        readWriteLock.writeLock().lock();
        V removeValue = null;
        try {
            removeValue = map.remove(key);
        } finally {
            readWriteLock.writeLock().unlock();
        }
        return removeValue;
    }
}