package com.example.music_board_spring.repository;

import com.example.music_board_spring.model.entity.Boards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository <Boards, Integer> {

    // boardName이 주어진 게시판 이름과 일치하는 경우의 수를 세어서, 그 수가 0보다 크면 true, 그렇지 않으면 false를 반환
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Boards b WHERE b.boardName = ?1")
    boolean existsByBoardName(String boardName);    //게시판 이름의 존재유무 확인

//    @Query("SELECT * FROM boards WHERE board_name = ?") //게시판 이름 검색
//    Optional<Boards> findByBoardName(String boardName);

    @Query("SELECT b FROM Boards b WHERE b.boardName = ?1")  //게시판 이름 검색
    Optional<Boards> findByBoardName(String boardName);
}
