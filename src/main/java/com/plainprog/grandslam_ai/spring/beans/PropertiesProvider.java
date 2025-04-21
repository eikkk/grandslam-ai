package com.plainprog.grandslam_ai.spring.beans;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesProvider {

    @Value("${getimg.api.key}")
    private String getImgApiKey;

    public String getImgApiKey() {
        return getImgApiKey;
    }
}