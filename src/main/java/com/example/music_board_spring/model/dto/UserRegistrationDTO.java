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

    public Users toUser() {
        Users user = new Users();
        user.setUsername(this.username);
        user.setEmail(this.email);
        user.setPasswordHash(this.password); // 이 비밀번호는 서비스 레이어에서 암호화됨
        user.setProfilePicture(this.profilePicture);
        return user;
    }
}