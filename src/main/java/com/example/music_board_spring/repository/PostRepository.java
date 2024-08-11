package com.example.music_board_spring.repository;


import com.example.music_board_spring.model.entity.Posts;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository <Posts, Long> {

    @Query("SELECT p FROM Posts p WHERE p.user.userId = :userId")
    List<Posts> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT p FROM Posts p WHERE p.postType = :boardName AND p.postId = :postId")
    Optional<Posts> findByBoardNameandId(String boardName, Long postId);

    @Query("SELECT p FROM Posts p WHERE p.board.boardName = :boardName")
    List<Posts> findAllByBoardName(String boardName, Pageable pageable);
}
