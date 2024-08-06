package com.example.music_board_spring.service;


import com.example.music_board_spring.exception.BoardAlreadyExistsException;
import com.example.music_board_spring.model.entity.Boards;
import com.example.music_board_spring.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    //게시판 생성
    public Boards createBoard(Boards boards){
        if (boardRepository.existsByBoardName(boards.getBoardName())) {
            throw new BoardAlreadyExistsException("이미 같은 이름의 게시판이 존재합니다.");
        }

        return boardRepository.save(boards);
    }

    //게시판 삭제
    public void deleteBoard(String boardname) {
        Boards board = boardRepository.findByBoardName(boardname)
                .orElseThrow(()-> new RuntimeException(boardname+" 게시판을 찾을 수 없습니다."));

        boardRepository.delete(board);
    }

    //게시판 수정
    public Boards updateBoard(String oldBoardName, String newBoardName){
        Boards board = boardRepository.findByBoardName(oldBoardName)
                .orElseThrow(()-> new RuntimeException(oldBoardName+"게시판을 찾을 수 없습니다."));

        board.setBoardName(newBoardName);
        return boardRepository.save(board);
    }

    public Optional<Boards> findbyBoardName(String boardName){
        return boardRepository.findByBoardName(boardName);
    }

    //전체 게시판 목록 조회
    public List<Boards> getAllBoards(){
       return boardRepository.findAll();
    }

    //게시판 통계

}
