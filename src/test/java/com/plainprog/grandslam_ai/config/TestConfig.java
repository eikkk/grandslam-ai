package com.plainprog.grandslam_ai.config;

import com.plainprog.grandslam_ai.service.account.helper.TestUserHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class TestConfig {

    @Bean
    public TestUserHelper testUserHelper() {
        return new TestUserHelper();
    }
}
