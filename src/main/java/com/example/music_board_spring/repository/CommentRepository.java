package com.example.music_board_spring.repository;


import com.example.music_board_spring.model.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository <Comments, Integer> {
}
