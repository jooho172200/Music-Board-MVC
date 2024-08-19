package com.example.music_board_spring.controller;

import com.example.music_board_spring.model.dto.ReportedDTO;
import com.example.music_board_spring.model.entity.Users;
import com.example.music_board_spring.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/reported-posts")
    public ResponseEntity<List<ReportedDTO>> getReportedPost(){
        List<ReportedDTO> reportedDTOS = adminService.getReportedPosts();
        return ResponseEntity.ok(reportedDTOS);
    }

    @GetMapping("/reported-comments")
    public ResponseEntity<List<ReportedDTO>> getReportedComment(){
        List<ReportedDTO> reportedDTOS = adminService.getReportedComments();
        return ResponseEntity.ok(reportedDTOS);
    }

    @PutMapping("/users/{userId}/activate")
    public ResponseEntity<Users> activateUser(@PathVariable Long userId){
        Users activatedUser = adminService.activateUser(userId);
        return ResponseEntity.ok(activatedUser);
    }

    @PutMapping("/users/{userId}/deactivate")
    public ResponseEntity<Users> deactivateUser(@PathVariable Long userId){
        Users deactivatedUser = adminService.deactivateUser(userId);
        return ResponseEntity.ok(deactivatedUser);
    }
}
