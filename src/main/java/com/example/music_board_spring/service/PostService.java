package com.example.music_board_spring.service;


import com.example.music_board_spring.exception.UserNotFoundException;
import com.example.music_board_spring.model.dto.PostDTO;
import com.example.music_board_spring.model.entity.Posts;
import com.example.music_board_spring.model.entity.Users;
import com.example.music_board_spring.repository.PostRepository;
import com.example.music_board_spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    //글 게시
    public PostDTO createPost(PostDTO postDTO){
        Posts post = convertToEntity(postDTO);
        Posts savedPost = postRepository.save(post);
        return convertToDto(savedPost);
    }

    //글 삭제
    public void deletePost(Posts post){
        postRepository.delete(post);
    }

    //글 수정
    public PostDTO updatePost(Long postId, PostDTO postDTO){
        Posts post = postRepository.findById(postId)
                .orElseThrow(()-> new RuntimeException("게시글을 찾을 수 없습니다. "));

        updatePostFromDto(post, postDTO);

        Posts updatedPost = postRepository.save(post);
        return convertToDto(updatedPost);

    }

    private void updatePostFromDto(Posts post, PostDTO postDto) {
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setPostType(postDto.getPostType());
        post.setPrice(postDto.getPrice());
        // user와 board는 별도로 설정?
    }

    //DTO를 엔티티로 변환
    private Posts convertToEntity(PostDTO postDto) {
        Posts post = new Posts();
        updatePostFromDto(post, postDto);
        return post;
    }

    //엔티티를 DTO로 변환
    private PostDTO convertToDto(Posts post) {
        PostDTO postDto = new PostDTO();
        postDto.setPostId(post.getPostId());
        postDto.setUserId(post.getUser().getUserId());
        postDto.setBoardId(post.getBoard().getBoardId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setPostType(post.getPostType());
        postDto.setPrice(post.getPrice());
        postDto.setCreatedAt(post.getCreatedAt());
        postDto.setUpdatedAt(post.getUpdatedAt());
        return postDto;
    }



    //파일 첨부


    //파일 다운로드


    //유저 작성글 불러오기
    public List<PostDTO> getUserPosts(Integer id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저입니다."));
        List<Posts> posts = postRepository.findByUserId(user);
        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    //전체 글 목록 가져오기
    public List<PostDTO> getAllPosts(){
        List<Posts> posts = postRepository.findAll();
        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    //작성 번호로 글 가져오기
    public PostDTO getPostById(Long postId) {
        Posts post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        return convertToDto(post);
    }
}
