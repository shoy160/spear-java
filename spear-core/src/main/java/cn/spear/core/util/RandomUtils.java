package cn.spear.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author shay
 * @date 2020/9/8
 */
public class RandomUtils {

    public static int randomInteger(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    public static String randomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static String fastId() {
        return UUID.randomUUID().toString();
    }

    public static <T> T randomGet(Collection<T> collections){
        int size = collections.size();
        Random random = new Random();
        return new ArrayList<>(collections).get(random.nextInt(size));
    }
}
