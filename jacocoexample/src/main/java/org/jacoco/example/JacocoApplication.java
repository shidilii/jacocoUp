package org.jacoco.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JacocoApplication {

    public static void main(String[] args) {

        SpringApplication.run(JacocoApplication.class, args);
        System.out.println("服务启动完成");
    }

}
