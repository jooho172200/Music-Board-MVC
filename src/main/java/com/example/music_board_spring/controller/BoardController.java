package com.example.music_board_spring.controller;


import com.example.music_board_spring.model.entity.Boards;
import com.example.music_board_spring.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService){
        this.boardService = boardService;
    }

    //게시판 생성
    @PostMapping
    public ResponseEntity<?> createBoard(@RequestBody Boards board){
        return ResponseEntity.ok(boardService.createBoard(board));
    }

    //게시판 삭제
    @DeleteMapping("/{boardName}")
    public ResponseEntity<?> deleteBoard(@PathVariable String boardName){
        boardService.deleteBoard(boardName);
        return ResponseEntity.ok().build();
    }

    //게시판 수정
    @PutMapping("/{boardName}")
    public ResponseEntity<?> updateBoard(@PathVariable String oldBoardName, @RequestBody String newBoardName){
        return ResponseEntity.ok(boardService.updateBoard(oldBoardName, newBoardName));
    }

    //게시판 이름으로 게시판 조회
    @GetMapping("/{boardName}")
    public ResponseEntity<?> findByBoardName(@PathVariable String boardName){
        return boardService.findbyBoardName(boardName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //전체 게시판 조회
    @GetMapping
    public ResponseEntity<List<Boards>> getAllBoards(){
        return ResponseEntity.ok(boardService.getAllBoards());
    }

}
