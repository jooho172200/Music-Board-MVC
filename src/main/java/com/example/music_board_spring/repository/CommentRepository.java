package com.example.music_board_spring.repository;


import com.example.music_board_spring.model.entity.Comments;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository <Comments, Long> {

    @Query("SELECT c FROM Comments c WHERE c.user.userId = :userId")
    List<Comments> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT c FROM Comments c WHERE c.post.postId = :postId")
    List<Comments> findByPostId(Long postId, Pageable pageable);
}
