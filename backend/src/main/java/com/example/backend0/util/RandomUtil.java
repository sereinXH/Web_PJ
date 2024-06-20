package com.example.backend0.util;

import java.util.Random;

/**
 * @ClassName Random
 * @Description
 **/
public class RandomUtil {
    public static int generateRandomInt(int a, int b) {
        if (a >= b) {
            throw new IllegalArgumentException("Invalid range. 'a' must be less than 'b'.");
        }

        Random random = new Random();
        return random.nextInt(b - a) + a;
    }
    public static float generateRandomFloat(int a, int b) {
        if (a >= b) {
            throw new IllegalArgumentException("Invalid range: a must be less than b");
        }

        Random random = new Random();
        float randomFloat = random.nextFloat();

        // 将随机数映射到 [a, b) 范围内
        return a + randomFloat * (b - a);
    }
}
