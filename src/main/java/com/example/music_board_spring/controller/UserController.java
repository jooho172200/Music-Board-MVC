package com.example.music_board_spring.controller;


import com.example.music_board_spring.exception.UserNotFoundException;
import com.example.music_board_spring.model.dto.UserLoginDTO;
import com.example.music_board_spring.model.dto.UserRegistrationDTO;
import com.example.music_board_spring.model.dto.UserUpdateDTO;
import com.example.music_board_spring.model.entity.Users;
import com.example.music_board_spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //회원가입
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        try {
            Users user = userService.registerUser(registrationDTO);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    //spring security에서 제공하는 기능
//
//    //로그인
//    @PostMapping("/login")
//    public ResponseEntity<?> loginUser(@RequestBody UserLoginDTO loginDTO) {
//        try {
//            Users user = userService.authenticateUser(loginDTO.getUsername(), loginDTO.getPassword());
//            return ResponseEntity.ok(user);
//        } catch (BadCredentialsException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
//        }
//    }

    //id로 사용자 찾기
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        try {
            return userService.findById(id)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new UserNotFoundException("ID가 " + id + "인 사용자를 찾을 수 없습니다."));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //모든 사용자 목록
    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    //유저 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody UserUpdateDTO updateDTO) {
        try {
            Users updatedUser = userService.updateUserInfo(id, updateDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //사용자 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("사용자가 성공적으로 삭제되었습니다.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //사용자 활성화
    @PutMapping("/{id}/activate")
    public ResponseEntity<?> activateUser(@PathVariable Integer id) {
        try {
            Users activatedUser = userService.activateUser(id);
            return ResponseEntity.ok(activatedUser);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //사용자 비활성화
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateUser(@PathVariable Integer id) {
        try {
            Users deactivatedUser = userService.deactivateUser(id);
            return ResponseEntity.ok(deactivatedUser);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
