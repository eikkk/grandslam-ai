package com.plainprog.grandslam_ai.service.account.helper;

import org.apache.commons.lang3.RandomStringUtils;

public class PassGenHelp {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!_@#*()";

    public static String randomPassword() {
        return RandomStringUtils.random(12, CHARACTERS);
    }
    public static String randomEmailVerificationToken() {
        return RandomStringUtils.random(64, CHARACTERS);
    }
}
