package com.example.music_board_spring.controller;

import com.example.music_board_spring.model.dto.ReportedDTO;
import com.example.music_board_spring.model.entity.Users;
import com.example.music_board_spring.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/reported-posts")
    public String getReportedPost(Model model){
        List<ReportedDTO> reportedDTOS = adminService.getReportedPosts();
        model.addAttribute("repoertedPosts", reportedDTOS);
        return "reportedPostList";
    }

    @GetMapping("/reported-comments")
    public String getReportedComment(Model model){
        List<ReportedDTO> reportedDTOS = adminService.getReportedComments();
        model.addAttribute("reportedComments", reportedDTOS);
        return "reportedCommentList";
    }

    @PutMapping("/users/{userId}/activate")
    public String activateUser(@PathVariable Long userId, Model model){
        Users activatedUser = adminService.activateUser(userId);
        model.addAttribute("user", activatedUser);
        return "userDetail";
    }

    @PutMapping("/users/{userId}/deactivate")
    public String deactivateUser(@PathVariable Long userId, Model model){
        Users deactivatedUser = adminService.deactivateUser(userId);
        model.addAttribute("user", deactivatedUser);
        return "userDetail";
    }
}
