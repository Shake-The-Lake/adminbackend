package com.example.shakethelakebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShakeTheLakeBackendApplication {

    protected ShakeTheLakeBackendApplication() {
        // This constructor is protected to prevent instantiation
    }

    public static void main(String[] args) {
        SpringApplication.run(ShakeTheLakeBackendApplication.class, args);
    }

}
