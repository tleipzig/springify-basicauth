package com.it_surmann.springify.basicauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


/**
 * A Spring Boot application class to support testing.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.it_surmann.springify"})
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

}
