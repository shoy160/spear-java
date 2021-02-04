package cn.spear.core.cache;

import cn.spear.core.lang.Action;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author shay
 * @date 2020/12/15
 */
public interface Cache<K, V> {
    /**
     * 添加缓存
     *
     * @param key   key
     * @param value value
     * @param time  expiration time
     * @param unit  unit
     */
    default void put(K key, V value, int time, TimeUnit unit) {
        long expireTime = System.currentTimeMillis() + unit.toMillis(time);
        put(key, value, expireTime);
    }

    /**
     * 添加缓存
     *
     * @param key        key
     * @param value      value
     * @param expireTime expire
     */
    default void put(K key, V value, Date expireTime) {
        put(key, value, expireTime.getTime());
    }

    /**
     * 添加缓存
     *
     * @param key        key
     * @param value      value
     * @param expireTime expire
     */
    void put(K key, V value, long expireTime);

    /**
     * 批量添加缓存
     *
     * @param map  缓存Map
     * @param time expiration time
     * @param unit timeunit
     */
    default void putAll(Map<K, V> map, int time, TimeUnit unit) {
        long expireTime = System.currentTimeMillis() + unit.toMillis(time);
        putAll(map, expireTime);
    }

    /**
     * 批量添加缓存
     *
     * @param map        缓存Map
     * @param expireTime expiration time
     */
    default void putAll(Map<K, V> map, Date expireTime) {
        putAll(map, expireTime.getTime());
    }

    /**
     * 批量添加缓存
     *
     * @param map        缓存Map
     * @param expireTime expiration time
     */
    default void putAll(Map<K, V> map, long expireTime) {
        for (K key : map.keySet()) {
            put(key, map.get(key), expireTime);
        }
    }

    /**
     * 获取缓存
     *
     * @param key key
     * @return cache value
     */
    V get(K key);

    /**
     * 获取缓存
     *
     * @param key             key
     * @param mappingFunction the function to compute a value
     * @param expireTime      expiration time
     * @return cached value
     */
    V getOrPut(K key, Function<? super K, ? extends V> mappingFunction, long expireTime);


    /**
     * 删除缓存
     *
     * @param key key
     */
    void remove(K key);

    /**
     * 请客所有缓存
     */
    void clean();

    void info();

    /**
     * 缓存键过期事件
     *
     * @param expiredAction action
     */
    void keyExpired(Action<K> expiredAction);
}
