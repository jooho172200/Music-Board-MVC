package com.example.music_board_spring.controller;

import com.example.music_board_spring.model.dto.CommentDTO;
import com.example.music_board_spring.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    //댓글 게시
    @PostMapping
    public String createComment(@PathVariable Long postId, @RequestParam Long userId, @RequestParam String content, Model model){
        CommentDTO commentDTO = commentService.createComment(postId, userId, content);
        model.addAttribute("comment", commentDTO);
        return "commentDetail";
    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId, Authentication authentication){
        commentService.deleteComment(commentId, authentication);
        return "redirect:/posts/" + postId + "comments";
    }

    //댓글 수정
    @PutMapping("/{commentId}")
    public String updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestParam CommentDTO commentDTO, Authentication authentication, Model model){
        CommentDTO updatedCommentDto = commentService.updateComment(commentId, commentDTO, authentication);
        model.addAttribute("comment", commentDTO);
        return "commentDetail";
    }

    //모든 댓글 불러오기
    @GetMapping
    public String getAllComments(@PathVariable Long postId, Pageable pageable, Model model){
        List<CommentDTO> commentDTOS = commentService.getAllComments(postId,pageable);
        model.addAttribute("comments", commentDTOS);
        return "commentList";
    }

    //유저 아이디로 댓글 불러오기
    @GetMapping("/users/{userId}")
    public String getUserComments(@PathVariable Long userId, Pageable pageable, Model model){
        List<CommentDTO> commentDTOS = commentService.getUserComments(userId, pageable);
        model.addAttribute("comments", commentDTOS);
        return "commentList";
    }

}
