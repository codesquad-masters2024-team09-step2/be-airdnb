package com.airdnb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AirDnbApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirDnbApplication.class, args);
    }

}
