package com.example.music_board_spring.model.dto;

import com.example.music_board_spring.model.entity.Users;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDTO {
    private String username;
    private String email;
    private String password;
    private String profilePicture;

}