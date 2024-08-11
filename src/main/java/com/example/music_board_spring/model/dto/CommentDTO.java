package com.example.music_board_spring.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class CommentDTO {
    private Long commentId;
    private Long postId;
    private Long userId;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
