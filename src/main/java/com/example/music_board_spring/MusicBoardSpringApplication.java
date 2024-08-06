package com.example.music_board_spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class MusicBoardSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicBoardSpringApplication.class, args);
    }

}
