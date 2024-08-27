package com.example.music_board_spring.controller;

import com.example.music_board_spring.model.dto.MarketPostDTO;
import com.example.music_board_spring.model.dto.PostDTO;
import com.example.music_board_spring.model.entity.Users;
import com.example.music_board_spring.service.PostService;
import com.example.music_board_spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board/{boardName}")
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    //게시글 작성 폼
    @GetMapping("/posts/new")
    public String showCreatePostForm(@PathVariable String boardName, Model model) {
        model.addAttribute("postDTO", "market".equals(boardName) ? new MarketPostDTO() : new PostDTO());
        model.addAttribute("boardName", boardName);
        return "posts/createPost";
    }

    //게시글 생성
    @PostMapping("/posts")
    public String createPost(@PathVariable String boardName,
                             @ModelAttribute("postDTO") PostDTO postDTO,
                             @RequestParam(value = "price", required = false) BigDecimal price,
                             @RequestParam(value = "file", required = false) MultipartFile file,
                             Authentication authentication,
                             Model model) {
        try {
            Users currentUser = (Users) authentication.getPrincipal();
            PostDTO createdPost;

            postDTO.setUserId(currentUser.getUserId());
            postDTO.setPostType(boardName);

            if ("market".equals(boardName)) {
                MarketPostDTO marketPostDTO = new MarketPostDTO();
                BeanUtils.copyProperties(postDTO, marketPostDTO);
                marketPostDTO.setPrice(price);
                createdPost = postService.createPost(marketPostDTO, file);
            } else {
                createdPost = postService.createPost(postDTO, file);
            }

            return "redirect:/board/" + boardName + "/posts/" + createdPost.getPostId();
        } catch (Exception e) {
            logger.error("Error creating post", e);
            model.addAttribute("error", "글 작성 중 오류가 발생했습니다: " + e.getMessage());
            return "posts/createPost";
        }
    }

    //게시글 삭제
    @DeleteMapping("/posts/{postId}")
    public String deletePost(@PathVariable String boardName, @PathVariable Long postId, Authentication authentication) {
        try {
            postService.deletePost(boardName, postId, authentication);
            return "redirect:/board/" + boardName;
        } catch (Exception e) {
            logger.error("Error deleting post", e);
            return "redirect:/board/" + boardName + "?error=삭제 중 오류가 발생했습니다";
        }
    }

    //게시글 수정 폼
    @GetMapping("/posts/{postId}/edit")
    public String showUpdatePostForm(@PathVariable String boardName, @PathVariable Long postId, Model model) {
        try {
            PostDTO postDTO = postService.getPostById(boardName, postId);
            model.addAttribute("postDTO", postDTO);
            model.addAttribute("boardName", boardName);
            return "posts/updatePost";
        } catch (Exception e) {
            logger.error("Error showing update form", e);
            return "redirect:/board/" + boardName + "?error=수정 폼 로딩 중 오류가 발생했습니다";
        }
    }

    //게시글 수정      -> post 매핑으로 바꿔야함
    @PostMapping("/posts/{postId}")
    public String updatePost(@PathVariable String boardName, @PathVariable Long postId, @ModelAttribute("postDTO") Object postDTO,
                             Authentication authentication, Model model) {
        try {
            PostDTO updatedPost;

            if ("market".equals(boardName) && postDTO instanceof MarketPostDTO) {
                MarketPostDTO marketPostDTO = (MarketPostDTO) postDTO;
                marketPostDTO.setPostType(boardName);
                updatedPost = postService.updatePost(postId, marketPostDTO, authentication);
            } else {
                PostDTO genericPostDTO = (PostDTO) postDTO;
                genericPostDTO.setPostType(boardName);
                updatedPost = postService.updatePost(postId, genericPostDTO, authentication);
            }

            return "redirect:/board/" + boardName + "/posts/" + postId;
        } catch (Exception e) {
            logger.error("Error updating post", e);
            model.addAttribute("error", "수정 중 오류가 발생했습니다: " + e.getMessage());
            return "posts/updatePost";
        }
    }

    //게시판의 모든 글 불러오기
    @GetMapping
    public String getAllPostsByBoardName(@PathVariable String boardName, Pageable pageable, Model model) {
        try {
            List<? extends PostDTO> posts = postService.getAllPostsByBoardName(boardName, pageable);
            model.addAttribute("posts", posts);
            model.addAttribute("boardName", boardName);
            model.addAttribute("isMarket", "market".equals(boardName));
            return "boards/boardDetail";
        } catch (Exception e) {
            logger.error("Error getting all posts", e);
            model.addAttribute("error", "게시글 목록을 가져오는 중 오류가 발생했습니다");
            return "boards/boardDetail";
        }
    }

    //게시글 번호로 게시글 불러오기
    @GetMapping("/posts/{postId}")
    public String getPostsById(@PathVariable String boardName, @PathVariable Long postId, Model model) {
        try {
            PostDTO post = postService.getPostById(boardName, postId);
            model.addAttribute("post", post);
            model.addAttribute("boardName", boardName);
            model.addAttribute("isMarket", post instanceof MarketPostDTO);
            return "posts/postDetail";
        } catch (Exception e) {
            logger.error("Error getting post by id", e);
            return "redirect:/board/" + boardName + "?error=게시글을 찾을 수 없습니다";
        }
    }

    //유저 작성 게시글 불러오기
    @GetMapping("/posts/user/{userId}")
    public String getPostsByUserName(@PathVariable String boardName, @PathVariable Long userId, Pageable pageable, Model model) {
        try {
            List<? extends PostDTO> posts = postService.getPostsByUserId(boardName, userId, pageable);
            model.addAttribute("posts", posts);
            model.addAttribute("boardName", boardName);
            model.addAttribute("isMarket", "market".equals(boardName));
            return "posts/userPost";
        } catch (Exception e) {
            logger.error("Error getting posts by user", e);
            model.addAttribute("error", "사용자의 게시글을 가져오는 중 오류가 발생했습니다");
            return "posts/userPost";
        }
    }
}