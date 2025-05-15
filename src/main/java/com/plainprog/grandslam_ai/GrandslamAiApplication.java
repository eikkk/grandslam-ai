package com.plainprog.grandslam_ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@PropertySource("classpath:application-secrets.properties")
@EnableScheduling
public class GrandslamAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrandslamAiApplication.class, args);
	}

}
