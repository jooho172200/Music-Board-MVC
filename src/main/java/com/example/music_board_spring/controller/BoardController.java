package com.example.music_board_spring.controller;


import com.example.music_board_spring.model.entity.Boards;
import com.example.music_board_spring.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    //게시판 생성
    @PostMapping
    public String createBoard(@RequestBody Boards board, Model model){
        Boards createdBoard = boardService.createBoard(board);
        model.addAttribute("board", board);

        return "boardDetail";
    }

    //게시판 삭제
    @DeleteMapping("/{boardName}")
    public String deleteBoard(@PathVariable String boardName, Model model){
        boardService.deleteBoard(boardName);
        return "redirect:/boards";
    }

    //게시판 수정
    @PutMapping("/{boardName}")
    public String updateBoard(@PathVariable String oldBoardName, @RequestParam String newBoardName,Model model){
        Boards updatedBoard = boardService.updateBoard(oldBoardName, newBoardName);
        model.addAttribute("board", updatedBoard);
        return "boardDetail";
    }

    //게시판 이름으로 게시판 조회
    @GetMapping("/{boardName}")
    public String findByBoardName(@PathVariable String boardName, Model model){
        Boards board = boardService.findbyBoardName(boardName)
                        .orElseThrow(()-> new RuntimeException("게시판을 찾을 수 없습니다. "));

        model.addAttribute("board", board);
        return "boardDetail";
    }

    //전체 게시판 조회
    @GetMapping
    public String getAllBoards(Model model){
        List<Boards> boards = boardService.getAllBoards();
        model.addAttribute("boards", boards);

        return "boardList";
    }

}
