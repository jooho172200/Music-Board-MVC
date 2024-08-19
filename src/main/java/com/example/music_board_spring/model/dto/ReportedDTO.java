package com.example.music_board_spring.model.dto;

import lombok.Data;

@Data
public class ReportedDTO {
    private Long id;
    private String type;
    private String content;
    private int reportCount;
}
