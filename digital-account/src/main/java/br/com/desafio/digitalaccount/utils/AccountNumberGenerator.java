package br.com.desafio.digitalaccount.utils;

import java.util.concurrent.ThreadLocalRandom;

public final class AccountNumberGenerator {

    private static final int BASE_NUMBER_LENGTH = 6;
    private static final int DEFAULT_AGENCY_NUMBER = 1;

    public static String generate() {
        // 1. Generate the random base number
        long baseNumber = generateBaseNumber();

        // 2. Calculate the check digit
        int checkDigit = calculateMod11CheckDigit(String.valueOf(baseNumber));

        // 3. Format the complete account number
        // The format %06d ensures the number will have 6 digits, with leading zeros if necessary.
        return String.format("%0" + BASE_NUMBER_LENGTH + "d-%d", baseNumber, checkDigit);
    }

    private static long generateBaseNumber() {
        long min = (long) Math.pow(10, BASE_NUMBER_LENGTH - 1);
        long max = (long) Math.pow(10, BASE_NUMBER_LENGTH) - 1;
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }

    private static int calculateMod11CheckDigit(String baseNumber) {
        int weightedSum = 0;
        int weight = 2;

        // Iterate over the digits of the base number, from right to left.
        for (int i = baseNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(baseNumber.charAt(i));
            weightedSum += digit * weight;

            // The weights for Modulo 11 cycle from 2 to 9.
            weight = (weight == 9) ? 2 : weight + 1;
        }

        int remainder = weightedSum % 11;
        int checkDigit = 11 - remainder;

        // If the result is 10 or 11, the check digit becomes 0.
        return (checkDigit >= 10) ? 0 : checkDigit;
    }

    public static String getDefaultAgency() {
        return String.format("%04d", DEFAULT_AGENCY_NUMBER);
    }
}