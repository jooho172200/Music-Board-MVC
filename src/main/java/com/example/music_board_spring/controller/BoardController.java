package com.example.music_board_spring.controller;

import com.example.music_board_spring.model.dto.BoardDTO;
import com.example.music_board_spring.model.entity.Boards;
import com.example.music_board_spring.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    //게시판 생성 폼
    @GetMapping("/new")
    public String showCreateBoardForm(Model model){
        model.addAttribute("boardDTO", new BoardDTO());
        return "boards/createBoard";
    }

    //게시판 생성
    @PostMapping
    public String createBoard(@ModelAttribute BoardDTO boardDTO, Model model){
        Boards board = new Boards();
        board.setBoardName(boardDTO.getBoardName());
        board.setDisplayName(boardDTO.getDisplayName());
        Boards createdBoard = boardService.createBoard(board);
        model.addAttribute("board", createdBoard);

        return "redirect:/boards/" + createdBoard.getBoardName();
    }

//    //게시글 삭제 폼
//    @GetMapping("/delete")
//    public String showDeleteBoardForm(Model model){
//        List<BoardDTO> boards = boardService.getAllBoards();
//        model.addAttribute("boards", boards);
//        return "boards/deleteBoard";
//    }

    //게시판 삭제
    @PostMapping("delete/{boardName}")
    public String deleteBoard(@PathVariable String boardName, Model model){
        boardService.deleteBoard(boardName);
        return "redirect:/admin";
    }

    //게시판 수정 폼
    @GetMapping("/{boardName}/update")
    public String showUpdateBoardForm(@PathVariable String boardName, Model model){
        Boards board = boardService.findbyBoardName(boardName)
                .orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다."));
        model.addAttribute("board", board);
        return "boards/UpdateBoard";
    }

    //게시판 수정
    @PostMapping("update/{oldBoardName}")
    public String updateBoard(@PathVariable String oldBoardName, @RequestParam String newBoardName, @RequestParam String newDisplayName, Model model){
        Boards updatedBoard = boardService.updateBoard(oldBoardName, newBoardName, newDisplayName);
        model.addAttribute("board", updatedBoard);
        return "redirect:/admin";
    }

    //게시판 이름으로 게시판 조회
    @GetMapping("/{boardName}")
    public String findByBoardName(@PathVariable String boardName, Model model){
        Boards board = boardService.findbyBoardName(boardName)
                .orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다."));
        model.addAttribute("board", board);
        return "boards/boardDetail";
    }

    //전체 게시판 조회
    @GetMapping
    public String getAllBoards(Model model){
        List<BoardDTO> boards = boardService.getAllBoards();
        model.addAttribute("boards", boards);
        return "boards/boardList";
    }
}