package com.roc.core.Utils.cache;

/**
 * User: rocwu
 * Date: 2016/11/17
 * Time: 19:31
 * Desc: 缓存策略类型
 */
public enum CacheStrategy {
    FIFO,   // 先进先出
    LFU,    // 最不经常使用
    LRU,    // 最近最少使用
}