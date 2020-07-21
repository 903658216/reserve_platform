package com.jh.microqiangquan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.jh")
@EnableEurekaClient
@EnableTransactionManagement
@EnableScheduling
public class MicroQiangQuanApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroQiangQuanApplication.class, args);
    }

}
