package com.example.music_board_spring.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {
    private String username;
    private String email;
    private String newPassword;
    private String profilePicture;
}
