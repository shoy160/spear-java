package cn.spear.core.test;

import cn.spear.core.util.RandomUtils;
import cn.spear.core.util.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Random;

/**
 * @author shay
 * @date 2020/9/7
 */
@Slf4j
public class StreamUtilsTest {

    @Test
    public void compressTest() {
        int length = new Random().nextInt(500);
        byte[] data = RandomUtils.randomString(201).getBytes();
        log.info("raw length:{}", data.length);
        if (data.length > 200) {
            data = StreamUtils.gzip(data);
            log.info("gzip compress length:{}", data.length);
        }
        //解压
        boolean isGzip = StreamUtils.isGzip(data);
        log.info("isGzip:{}", isGzip);
        if (isGzip) {
            data = StreamUtils.unGzip(data);
        }
        log.info("raw str:{}", new String(data));
    }
}
