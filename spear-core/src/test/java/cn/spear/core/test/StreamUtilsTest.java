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
        int length = new Random().nextInt(5000);
        byte[] data = RandomUtils.randomString(length).getBytes();
        if (data.length > 200) {
            data = StreamUtils.gzip(data);
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
