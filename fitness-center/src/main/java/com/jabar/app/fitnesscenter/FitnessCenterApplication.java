package com.jabar.app.fitnesscenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FitnessCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitnessCenterApplication.class, args);
    }

}
