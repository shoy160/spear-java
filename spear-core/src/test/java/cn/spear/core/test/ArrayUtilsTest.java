package cn.spear.core.test;

import cn.spear.core.service.ServiceAddress;
import cn.spear.core.util.ArrayUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.*;

/**
 * @author shay
 * @date 2020/9/9
 */
@Slf4j
public class ArrayUtilsTest {

    private ServiceAddress[] getArray() {
        ServiceAddress[] array = new ServiceAddress[3];
        array[0] = new ServiceAddress("127.0.0.1", 1001) {
            {
                setWeight(3);
            }
        };
        array[1] = new ServiceAddress("127.0.0.1", 1002) {
            {
                setWeight(2);
            }
        };
        array[2] = new ServiceAddress("127.0.0.1", 1003);
        return array;
    }

    @Test
    public void randomSortTest() {
        ServiceAddress[] array = getArray();
        ArrayUtils.randomSort(array);
        log.info("ports:{},{},{}", array[0].getPort(), array[1].getPort(), array[2].getPort());
    }

    @Test
    public void randomTest() {
        ServiceAddress[] array = getArray();
        int[] state = new int[3];
        for (int i = 0; i < 1000; i++) {
            ServiceAddress address = ArrayUtils.random(array);
            if (address != null) {
                state[address.getPort() - 1001] += 1;
//                log.info("current port:{}", address.getPort());
            }
        }
        log.info("1001:{},1002:{},1003:{}", state[0], state[1], state[2]);
    }

    @Test
    public void randomWeightTest() {
        ServiceAddress[] array = getArray();
        int[] state = new int[3];
        for (int i = 0; i < 1000; i++) {
            ServiceAddress address = ArrayUtils.randomWeight(Arrays.asList(array));
            if (address != null) {
                state[address.getPort() - 1001] += 1;
//                log.info("current port:{}", address.getPort());
            }
        }
        log.info("1001:{},1002:{},1003:{}", state[0], state[1], state[2]);
    }
}
