package cn.spear.core.test;

import cn.spear.core.util.CommonUtils;
import cn.spear.core.util.PathUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Util 测试
 *
 * @author shay
 * @date 2020/9/4
 */
@Slf4j
public class CommonUtilsTest {

    @Test
    public void isEmptyForStringTest() {
        String str = "";
        Assert.assertTrue(CommonUtils.isEmpty(str));
        str = "123";
        Assert.assertFalse(CommonUtils.isEmpty(str));
    }

    @Test
    public void isEmptyForMapTest() {
        Map<String, Object> map = new HashMap<>();
        Assert.assertTrue(CommonUtils.isEmpty(map));
        map.put("key", 12345);
        Assert.assertFalse(CommonUtils.isEmpty(map));
    }

    @Test
    public void isEmptyForListTest() {
        List<String> list = new ArrayList<>();
        Assert.assertTrue(CommonUtils.isEmpty(list));
        list.add("shay");
        Assert.assertFalse(CommonUtils.isEmpty(list));
    }

    @Test
    public void isEmptyForArrayTest() {
        String[] array = new String[0];
        Assert.assertTrue(CommonUtils.isEmpty(array));
        array = new String[1];
        array[0] = "shay";
        Assert.assertFalse(CommonUtils.isEmpty(array));
    }

    @Test
    public void fileNameTest() {
        String path = "dfd/sdfsdf\\sdfsdf\\test.ts.tar";
        String name = PathUtils.getExt(path);
        Assert.assertEquals(name, "tar");
    }

    @Test
    public void timeoutTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        try {
            Future<Object> future = executorService.submit(() -> {
                try {
                    return executeTask(1, 3);
                } catch (Throwable e) {
                    throw new ExecutionException(e);
                }
            });
            Object result = future.get(5, TimeUnit.SECONDS);
            log.info(result.toString());
        } catch (Throwable ex) {
            Throwable throwable = ex;
            if (throwable instanceof InterruptedException || throwable instanceof ExecutionException) {
                throwable = throwable.getCause();
            }
            throwable.printStackTrace();
        }
    }

    private Object executeTask(int a, int b) throws Throwable {
        Thread.sleep(6000L);
        return a + b;
    }
}
