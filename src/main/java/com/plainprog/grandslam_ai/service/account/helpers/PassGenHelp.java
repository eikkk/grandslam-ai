package com.plainprog.grandslam_ai.service.account.helpers;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PassGenHelp {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!_@#*()";

    public static String randomPassword() {
        return RandomStringUtils.random(12, CHARACTERS);
    }
    public static String randomSalt() {
        return BCrypt.gensalt();
    }
    public static String hashPassword(String pass, String salt) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        return encoder.encode(pass + salt);
    }
    public static String randomEmailVerificationToken() {
        return RandomStringUtils.random(64, CHARACTERS);
    }
}
