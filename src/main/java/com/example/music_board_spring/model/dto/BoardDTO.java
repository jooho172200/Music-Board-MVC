package com.example.music_board_spring.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private Integer boardId;
    private String boardName;
    private String displayName;
}