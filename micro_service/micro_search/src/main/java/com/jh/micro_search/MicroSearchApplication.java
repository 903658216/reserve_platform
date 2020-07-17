package com.jh.micro_search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.jh")
@EnableEurekaClient
@EnableScheduling
public class MicroSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroSearchApplication.class, args);
    }

}
