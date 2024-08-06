package com.example.music_board_spring.exception;

public class BoardAlreadyExistsException extends RuntimeException{
    
    // 게시판이 이미 존재할 때의 예외처리
    public BoardAlreadyExistsException(String message) {
        super(message);
    }
}
