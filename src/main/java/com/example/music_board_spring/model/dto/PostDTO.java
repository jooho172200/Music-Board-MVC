package com.example.music_board_spring.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
@Getter
@Setter
public class PostDTO {
    private Long postId;
    private Long userId;
    private String username;
    private Integer boardId;
    private String title;
    private String content;
    private String postType;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String filename;
    private String filepath;

}
