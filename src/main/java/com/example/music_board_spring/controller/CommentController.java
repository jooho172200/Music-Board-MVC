package com.example.music_board_spring.controller;

import com.example.music_board_spring.model.dto.CommentDTO;
import com.example.music_board_spring.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    //댓글 게시
    @PostMapping
    public ResponseEntity<?> createComment(@PathVariable Long postId, @RequestParam Long userId, @RequestBody String content){
        CommentDTO commentDTO = commentService.createComment(postId, userId, content);
        return ResponseEntity.ok(commentDTO);
    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long postId, @PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    //댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentDTO commentDTO){
        CommentDTO updatedCommentDto = commentService.updateComment(commentId, commentDTO);
        return ResponseEntity.ok(updatedCommentDto);
    }

    //모든 댓글 불러오기
    @GetMapping
    public ResponseEntity<?> getAllComments(@PathVariable Long postId, Pageable pageable){
        List<CommentDTO> commentDTOS = commentService.getAllComments(postId,pageable);
        return ResponseEntity.ok(commentDTOS);
    }

    //유저 아이디로 댓글 불러오기
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserComments(@PathVariable Long userId, Pageable pageable){
        List<CommentDTO> commentDTOS = commentService.getUserComments(userId, pageable);
        return ResponseEntity.ok(commentDTOS);
    }

}
