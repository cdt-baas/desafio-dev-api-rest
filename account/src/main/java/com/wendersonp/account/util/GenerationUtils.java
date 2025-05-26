package com.wendersonp.account.util;

import java.util.Random;

public class GenerationUtils {

    private GenerationUtils() {}

    private static final Random random = new Random();

    public static String generateAccountNumber() {
        return String.format("%010d", (random.nextLong(0, 9999999999L)));
    }
}
