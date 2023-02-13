package cn.spear.core.test;

import cn.spear.core.util.RandomUtils;
import cn.spear.core.util.BufferUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Random;

/**
 * @author shay
 * @date 2020/9/7
 */
@Slf4j
public class BufferUtilsTest {

    @Test
    public void compressTest() {
        int length = new Random().nextInt(5000);
        byte[] data = RandomUtils.randomStr(length).getBytes();
        if (data.length > 200) {
            data = BufferUtils.gzip(data);
        }
        //解压
        boolean isGzip = BufferUtils.isGzip(data);
        log.info("isGzip:{}", isGzip);
        if (isGzip) {
            data = BufferUtils.unGzip(data);
        }
        log.info("raw str:{}", new String(data));
    }

    @Test
    public void toHexTest() {
        int length = new Random().nextInt(15);
        byte[] data = RandomUtils.randomStr(length).getBytes();
        log.info("hex:{}", BufferUtils.toHex(data));
    }

    @Test
    public void sampleTest() {
        int num = RandomUtils.randomInt(1000);
        String hex = BufferUtils.toHex(BufferUtils.fromInt(num, false));
        String sampleHex = BufferUtils.toHex(BufferUtils.fromInt(num, true));
        log.info("num:{}, hex:{}, sample hex:{}", num, hex, sampleHex);
    }
}
