package com.raman.recruitr.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;

public class PasswordGenerator {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    public static String generate(int length) {
        return RandomStringUtils.random(
                length,
                0, 0,
                true, true,
                null,
                SECURE_RANDOM
        );
    }
}
