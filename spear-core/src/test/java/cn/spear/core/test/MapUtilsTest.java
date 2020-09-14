package cn.spear.core.test;

import cn.spear.core.message.MessageSerializer;
import cn.spear.core.util.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author shay
 * @date 2020/9/9
 */
@Slf4j
public class MapUtilsTest {
    private final Map<String, Object> map;

    public MapUtilsTest() {
        this.map = new HashMap<>();
        this.map.put("name", "小明");
        this.map.put("nick", "handsome");
        this.map.put("age", 18);
        this.map.put("amount", 520.0D);
    }

    @Test
    public void sortTest() {
        TreeMap<String, Object> sort = MapUtils.sort(map);
        Assert.assertEquals(sort.keySet().toArray()[3], "nick");
        TreeMap<String, Object> reverse = MapUtils.sort(map, Comparator.reverseOrder());
        Assert.assertEquals(reverse.keySet().toArray()[3], "age");
    }

    @Test
    public void filterTest() {
        Map<String, Object> filterKeys = MapUtils.filterKeys(map, "name", "age");
        Assert.assertEquals(filterKeys.size(), 2);
        Map<String, Object> filterValues = MapUtils.filterValues(map, 18, 520.1D);
        Assert.assertEquals(filterValues.size(), 1);
    }

    @Test
    public void joinTest() {
        String str = MapUtils.join(map, ",", "=", false);
        log.info("join : {}", str);

        str = MapUtils.sortJoinUrl(map, true);
        log.info("joinUrl : {}", str);

        str = MapUtils.joinUrlEncode(MapUtils.sort(map, Comparator.reverseOrder()), true);
        log.info("joinUrlEncode : {}", str);
    }
}
