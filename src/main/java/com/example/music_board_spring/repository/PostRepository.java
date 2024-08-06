package com.example.music_board_spring.repository;


import com.example.music_board_spring.model.entity.Posts;
import com.example.music_board_spring.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository <Posts, Long> {

    @Query("SELECT p FROM Posts p WHERE p.user = :user")
    List<Posts> findByUserId(Users user);

}
