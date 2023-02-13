package cn.spear.core.test;

import cn.spear.core.cache.Cache;
import cn.spear.core.cache.impl.MemoryCache;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author shay
 * @date 2020/12/15
 */
@Slf4j
public class CacheTest {
    private final Cache<String, String> cache;

    public CacheTest() {
        this.cache = new MemoryCache<>();
        this.cache.keyExpired(key -> {
            log.info("reload key:{}", key);
//            this.cache.put(key, "newValue", 10, TimeUnit.SECONDS);
        });
    }

    @Test
    public void setTest() throws InterruptedException {
        final String key = "shay";
        this.cache.put(key, "123", 20, TimeUnit.SECONDS);
        for (int i = 0; i < 10; i++) {
            String value = this.cache.get(key);
            log.info("get cache : {} => {}", key, value);
            TimeUnit.SECONDS.sleep(5);
        }
    }
}
