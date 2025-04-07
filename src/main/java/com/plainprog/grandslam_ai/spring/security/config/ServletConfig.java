package com.plainprog.grandslam_ai.spring.security.config;

import com.plainprog.grandslam_ai.service.account.helper.TestUserHelper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.SessionTrackingMode;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.EnumSet;

@Configuration
public class ServletConfig {

    //this is needed for the spring to stop issue a JSESSIONID cookie
    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                servletContext.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.URL));
            }
        };
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}