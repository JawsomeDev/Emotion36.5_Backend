package com.emotion_apiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EmotionApiserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmotionApiserverApplication.class, args);
    }

}
