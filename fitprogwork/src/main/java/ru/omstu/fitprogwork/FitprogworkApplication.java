package ru.omstu.fitprogwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FitprogworkApplication {
    public static void main(String[] args) {
        SpringApplication.run(FitprogworkApplication.class, args);
    }

}
