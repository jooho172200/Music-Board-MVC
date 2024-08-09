package com.example.music_board_spring.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MarketPostDTO extends PostDTO{
    //추후 필요에 따라 필드 더 추가
    private BigDecimal price;
}
