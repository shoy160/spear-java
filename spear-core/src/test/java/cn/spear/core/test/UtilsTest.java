package cn.spear.core.test;

import cn.spear.core.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Util 测试
 *
 * @author shay
 * @date 2020/9/4
 */
@Slf4j
public class UtilsTest {

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
}
