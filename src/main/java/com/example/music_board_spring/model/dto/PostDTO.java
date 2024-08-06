package com.example.music_board_spring.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@ToString
public class PostDTO {
    private Long postId;
    private Integer userId;
    private Integer boardId;
    private String title;
    private String content;
    private String postType;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String filename;
    private String filepath;
}
