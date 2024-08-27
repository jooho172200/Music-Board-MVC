package com.example.music_board_spring.controller;

import com.example.music_board_spring.model.dto.PostDTO;
import com.example.music_board_spring.model.entity.Users;
import com.example.music_board_spring.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;

    //메인 화면
    @GetMapping("/")
    public String showHomePage(Model model, Authentication authentication) {
        List<? extends PostDTO> freeBoardPosts = postService.getAllPostsByBoardName("free", Pageable.unpaged());
        List<? extends PostDTO> marketBoardPosts = postService.getAllPostsByBoardName("market", Pageable.unpaged());

        model.addAttribute("freeBoardPosts", freeBoardPosts);
        model.addAttribute("marketBoardPosts", marketBoardPosts);

        if (authentication != null && authentication.isAuthenticated()) {
            Users user = (Users) authentication.getPrincipal();
            model.addAttribute("username", user.getUsername());
            model.addAttribute("isAdmin", user.isAdmin());      //관리자 계정인지 확인
        }

        return "index";
    }

    //로그인 화면
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}