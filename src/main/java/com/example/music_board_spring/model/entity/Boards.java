package com.example.music_board_spring.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "boards")
@Getter
@Setter
@ToString
public class Boards {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer boardId;

    @Column(name = "board_name", nullable = false, length = 50)
    private String boardName;

    @Column(name = "display_name", nullable = false, length = 50)
    private String displayName;
}
