package com.mj.portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ArcadeHubServerApp {

    public static void main(String[] args) {
        SpringApplication.run(ArcadeHubServerApp.class, args);
    }
}
