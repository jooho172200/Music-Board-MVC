package com.example.music_board_spring.controller;

import com.example.music_board_spring.model.dto.PostDTO;
import com.example.music_board_spring.model.entity.Posts;
import com.example.music_board_spring.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board/{boardName}")
public class PostController {
    private final PostService postService;

    //게시글 작성
    @PostMapping("/posts")
    public ResponseEntity<?> createPost(@PathVariable String boardName, @RequestBody PostDTO postDTO){
        postDTO.setPostType(boardName);
        PostDTO createdPost = postService.createPost(postDTO);
        return ResponseEntity.ok(createdPost);
    }

    //게시글 삭제
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable String boardName, @PathVariable Long postId){
        postService.deletePost(boardName, postId);
        return ResponseEntity.ok().build();
    }

    //게시글 수정
    @PutMapping("/posts/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable String boardName, @PathVariable Long postId, @RequestBody PostDTO postDTO){
        postDTO.setPostType(boardName);
        PostDTO updatedPost = postService.updatePost(postId, postDTO);
        return ResponseEntity.ok(updatedPost);
    }

    //게시판의 모든 글 불러오기
    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPostsByBoardName(@PathVariable String boardName, Pageable pageable){
        List<PostDTO> postDTOS = postService.getAllPostsByBoardName(boardName,pageable);
        return ResponseEntity.ok(postDTOS);
    }

    //게시글 번호로 글 가져오기
    @GetMapping("/posts/{postId}")
    public ResponseEntity<?> getPostsById(@PathVariable String boardName, @PathVariable Long postId){
        PostDTO postDTOS = postService.getPostById(boardName, postId);
        return ResponseEntity.ok(postDTOS);
    }

    //유저 작성 게시물 가져오기
    @GetMapping("/posts/user/{userId}")
    public ResponseEntity<List<PostDTO>> getPostsByUserName(@PathVariable String boardName, @PathVariable Integer userId, Pageable pageable){
        List<PostDTO> postDTOS = postService.getPostsByUserId(boardName, userId, pageable);
        return ResponseEntity.ok(postDTOS);
    }

}
