package com.example.music_board_spring.controller;


import com.example.music_board_spring.exception.UserNotFoundException;
import com.example.music_board_spring.model.dto.UserRegistrationDTO;
import com.example.music_board_spring.model.dto.UserUpdateDTO;
import com.example.music_board_spring.model.entity.Users;
import com.example.music_board_spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //회원가입
    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserRegistrationDTO registrationDTO, Model model) {
        try {
            Users user = userService.registerUser(registrationDTO);
            model.addAttribute("user", user);
            return "userDetail";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "registerForm";
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
    @GetMapping("/{userid}")
    public String getUserById(@PathVariable Long userId, Model model) {
        try {
            Users user = userService.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("ID가 " + userId + "인 사용자를 찾을 수 없습니다."));
            model.addAttribute("user", user);

            return "userDetail";

        } catch (UserNotFoundException e) {

            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    //모든 사용자 목록
    @GetMapping
    public String getAllUsers(Model model) {
        List<Users> users = userService.findAll();
        model.addAttribute("users", users);
        return "userList";
    }

    //유저 정보 수정
    @PutMapping("/{userid}")
    public String updateUser(@PathVariable Long userId, @ModelAttribute UserUpdateDTO updateDTO, Model model) {
        try {
            Users updatedUser = userService.updateUserInfo(userId, updateDTO);
            model.addAttribute("user", updatedUser);

            return "userDetail";

        } catch (UserNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "updateForm";
        }
    }

    //사용자 삭제
    @DeleteMapping("/{userid}")
    public String deleteUser(@PathVariable Long userId, Model model) {
        try {
            userService.deleteUser(userId);
            return "redirect:/users";
        } catch (UserNotFoundException e) {

            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    //사용자 활성화
    @PutMapping("/{userid}/activate")
    public String activateUser(@PathVariable Long userId, Model model) {
        try {
            Users activatedUser = userService.activateUser(userId);
            model.addAttribute("user", activatedUser);
            return "userDetail";

        } catch (UserNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    //사용자 비활성화
    @PutMapping("/{userid}/deactivate")
    public String deactivateUser(@PathVariable Long userId, Model model) {
        try {
            Users deactivatedUser = userService.deactivateUser(userId);
            model.addAttribute("user", deactivatedUser);
            return "userDetail";

        } catch (UserNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

}
