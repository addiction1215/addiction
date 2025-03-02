package com.addiction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AddictionApplication {

    public static void main(String[] args) {
        SpringApplication.run(AddictionApplication.class, args);
    }

}
