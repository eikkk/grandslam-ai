package com.plainprog.grandslam_ai.config;

import com.plainprog.grandslam_ai.service.account.helper.TestHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class TestConfig {

    @Bean
    public TestHelper testUserHelper() {
        return new TestHelper();
    }
}
