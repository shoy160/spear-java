package cn.spear.core.util;

import java.util.Random;
import java.util.UUID;

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
}
