package org.ashlesha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.ashlesha")
public class Ashlesha{
    public static void main(String[] args) {
       SpringApplication.run(Ashlesha.class, args);
    }
}