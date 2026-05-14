package com.javaeasybank.customer.util;

import java.util.Locale;

public final class TaiwanIdValidator {

    private static final String LETTERS = "ABCDEFGHJKLMNPQRSTUVXYWZIO";

    private TaiwanIdValidator() {
    }

    public static String normalize(String idNumber) {
        return idNumber == null ? null : idNumber.trim().toUpperCase(Locale.ROOT);
    }

    public static boolean isValid(String idNumber) {
        String normalized = normalize(idNumber);
        if (normalized == null || !normalized.matches("^[A-Z][12][0-9]{8}$")) {
            return false;
        }

        int letterIndex = LETTERS.indexOf(normalized.charAt(0));
        if (letterIndex < 0) {
            return false;
        }

        int code = letterIndex + 10;
        int sum = (code / 10) + (code % 10) * 9;
        for (int i = 1; i <= 8; i++) {
            sum += Character.digit(normalized.charAt(i), 10) * (9 - i);
        }
        sum += Character.digit(normalized.charAt(9), 10);
        return sum % 10 == 0;
    }
}
