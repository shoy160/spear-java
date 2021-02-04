package cn.spear.core.cache.impl;

import cn.spear.core.cache.Cache;
import cn.spear.core.cache.CacheItem;
import cn.spear.core.lang.Action;
import cn.spear.core.lang.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.function.Function;

/**
 * @author shay
 * @date 2020/12/15
 */
@Slf4j
public class MemoryCache<K, V> implements Cache<K, V> {
    private final ConcurrentHashMap<K, CacheItem<V>> mapCache;

    private Action<K> expiredListener;

    public MemoryCache() {
        this(200);
    }

    public MemoryCache(int cleanInterval) {
        this(0, cleanInterval);
    }

    public MemoryCache(int cleanDelay, int cleanInterval) {
        log.info("init map cache");
        this.mapCache = new ConcurrentHashMap<>();
        ThreadFactory threadFactory = ThreadFactoryBuilder.create().setNamePrefix("memory-cache-").build();
        ScheduledExecutorService swapExpiredPool = new ScheduledThreadPoolExecutor(1, threadFactory);
        swapExpiredPool.scheduleAtFixedRate(this::cleanExpired, cleanDelay, cleanInterval, TimeUnit.MILLISECONDS);
    }

    @Override
    public void put(K key, V value, long expireTime) {
        CacheItem<V> item = new CacheItem<>(value, expireTime);
        this.mapCache.put(key, item);
    }

    @Override
    public V get(K key) {
        if (this.mapCache.containsKey(key)) {
            return this.mapCache.get(key).getValue();
        }
        return null;
    }

    @Override
    public V getOrPut(K key, Function<? super K, ? extends V> mappingFunction, long expireTime) {
        if (this.mapCache.containsKey(key)) {
            return this.mapCache.get(key).getValue();
        }
        V value = mappingFunction.apply(key);
        put(key, value, expireTime);
        return value;
    }

    @Override
    public void remove(K key) {
        this.mapCache.remove(key);
    }

    @Override
    public void clean() {
        this.mapCache.clear();
    }

    @Override
    public void info() {
        log.info("cache count:{}", this.mapCache.size());
    }

    @Override
    public void keyExpired(Action<K> expiredAction) {
        this.expiredListener = expiredAction;
    }

    /**
     * 清空已过期缓存
     */
    private void cleanExpired() {
        for (K key : this.mapCache.keySet()) {
            CacheItem<V> item = this.mapCache.get(key);
            long expireTime = item.getExpireTime();
            // <= 0 无过期时间
            if (expireTime <= 0 || expireTime > System.currentTimeMillis()) {
                continue;
            }
            log.info("clean key:{},hit:{}", key, item.getHitCount());
            this.mapCache.remove(key);
            if (this.expiredListener != null) {
                this.expiredListener.invoke(key);
            }
        }
    }
}
