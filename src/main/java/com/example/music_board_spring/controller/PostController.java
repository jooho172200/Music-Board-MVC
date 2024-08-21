package com.example.music_board_spring.controller;

import com.example.music_board_spring.model.dto.PostDTO;
import com.example.music_board_spring.service.PostService;
import com.example.music_board_spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board/{boardName}")
public class PostController {
    private final PostService postService;
    private final UserService userService;

    //게시글 작성 폼
    @GetMapping("/posts/new")
    public String showCreatePostForm(@PathVariable String boardName, Model model) {
        model.addAttribute("postDTO", new PostDTO());
        model.addAttribute("boardName", boardName);
        return "createPost";
    }

    //게시글 작성
    @PostMapping("/posts")
    public String createPost(@PathVariable String boardName, @ModelAttribute PostDTO postDTO, Model model){
        postDTO.setPostType(boardName);
        PostDTO createdPost = postService.createPost(postDTO);
        model.addAttribute("post", createdPost);
        return "redirect:/board/" + boardName;
    }

    //게시글 삭제
    @DeleteMapping("/posts/{postId}")
    public String deletePost(@PathVariable String boardName, @PathVariable Long postId, Authentication authentication){
        postService.deletePost(boardName, postId, authentication);
        return "redirect:/board/" + boardName;
    }

    // 게시글 수정 폼
    @GetMapping("/posts/{postId}/edit")
    public String showUpdatePostForm(@PathVariable String boardName, @PathVariable Long postId, Model model) {
        PostDTO postDTO = postService.getPostById(boardName, postId);
        model.addAttribute("postDTO", postDTO);
        model.addAttribute("boardName", boardName);
        return "updatePost";
    }

    //게시글 수정
    @PutMapping("/posts/{postId}")
    public String updatePost(@PathVariable String boardName, @PathVariable Long postId, @ModelAttribute PostDTO postDTO, Authentication authentication){
        postDTO.setPostType(boardName);
        PostDTO updatedPost = postService.updatePost(postId, postDTO, authentication);
        return "redirect:/board/" + boardName + "/posts/" + postId;
    }

    //게시판의 모든 글 불러오기
    @GetMapping
    public String getAllPostsByBoardName(@PathVariable String boardName, Pageable pageable, Model model){
        List<PostDTO> postDTOS = postService.getAllPostsByBoardName(boardName,pageable);
        model.addAttribute("posts", postDTOS);
        model.addAttribute("boardName", boardName);
        return "postList";
    }

    //게시글 번호로 글 가져오기
    @GetMapping("/posts/{postId}")
    public String getPostsById(@PathVariable String boardName, @PathVariable Long postId, Model model){
        PostDTO postDTOS = postService.getPostById(boardName, postId);
        model.addAttribute("posts", postDTOS);
        model.addAttribute("boardName", boardName);
        return "postDetail";
    }

    //유저 작성 게시물 가져오기
    @GetMapping("/posts/user/{userId}")
    public String getPostsByUserName(@PathVariable String boardName, @PathVariable Long userId, Pageable pageable, Model model){
        List<PostDTO> postDTOS = postService.getPostsByUserId(boardName, userId, pageable);
        model.addAttribute("posts", postDTOS);
        model.addAttribute("boardName", boardName);
        return "userPost";
    }

}
