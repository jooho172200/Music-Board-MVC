package com.example.music_board_spring.service;


import com.example.music_board_spring.exception.UserNotFoundException;
import com.example.music_board_spring.model.dto.MarketPostDTO;
import com.example.music_board_spring.model.dto.PostDTO;
import com.example.music_board_spring.model.entity.Posts;
import com.example.music_board_spring.model.entity.Users;
import com.example.music_board_spring.repository.PostRepository;
import com.example.music_board_spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    private final UserRepository userRepository;

    //글 게시
    @Transactional
    public  <T extends PostDTO> T createPost(T postDTO){
        Posts post = convertToEntity(postDTO);
        Posts savedPost = postRepository.save(post);
        return (T) convertToDto(savedPost, postDTO.getClass());
    }

    //글 삭제
    @Transactional
    public void deletePost(String boardName, Long postId){
        Posts post = postRepository.findByBoardNameandId(boardName, postId)
                .orElseThrow(()-> new RuntimeException("게시글을 찾을 수 없습니다. "));
        postRepository.delete(post);
    }

    //글 수정
    @Transactional
    public <T extends PostDTO> T updatePost(Long postId, T postDTO){
        Posts post = postRepository.findById(postId)
                .orElseThrow(()-> new RuntimeException("게시글을 찾을 수 없습니다. "));

        updatePostFromDto(post, postDTO);

        Posts updatedPost = postRepository.save(post);
        return (T) convertToDto(updatedPost, postDTO.getClass());

    }

    //DTO를 엔티티로
    private <T extends PostDTO> void updatePostFromDto(Posts post, T postDto) {
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setPostType(postDto.getPostType());

        if (postDto instanceof MarketPostDTO){
            post.setPrice(((MarketPostDTO) postDto).getPrice());
        }

        // user와 board는 별도로 설정?
    }

    //DTO를 엔티티로 변환
    private <T extends PostDTO> Posts convertToEntity(T postDto) {
        Posts post = new Posts();
        updatePostFromDto(post, postDto);
        return post;
    }

    //엔티티를 DTO로 변환
    private <T extends PostDTO> T convertToDto(Posts post, Class<T> dtoClass) {
        try {
            T postDto = dtoClass.getDeclaredConstructor().newInstance();
            postDto.setPostId(post.getPostId());
            postDto.setUserId(post.getUser().getUserId());
            postDto.setBoardId(post.getBoard().getBoardId());
            postDto.setTitle(post.getTitle());
            postDto.setContent(post.getContent());
            postDto.setPostType(post.getPostType());
            postDto.setCreatedAt(post.getCreatedAt());
            postDto.setUpdatedAt(post.getUpdatedAt());

            if (postDto instanceof MarketPostDTO) {
                ((MarketPostDTO) postDto).setPrice(post.getPrice());
            }

            return postDto;
        } catch (Exception e) {
            throw new RuntimeException("DTO 변환 중 오류가 발생했습니다.", e);
        }
    }


    //파일 업로드
    //파일 다운로드

    // 게시판 유형에 따라 DTO 클래스를 결정하는 메서드
    private Class<? extends PostDTO> determineDtoClass(String postType) {
        switch (postType) {
            case "market":
                return MarketPostDTO.class;
            // 다른 유형들에 대한 케이스 추가
            default:
                return PostDTO.class;
        }
    }

    //유저 작성글 불러오기
    @Transactional(readOnly = true)
    public List<PostDTO> getPostsByUserId(String boardType, Long userId, Pageable pageable) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저입니다."));
        List<Posts> posts = postRepository.findByUserId(user.getUserId(), pageable);
        return posts.stream()
                .map(post -> convertToDto(post, determineDtoClass(post.getPostType())))
                .collect(Collectors.toList());
    }

    //전체 게시글 반환
    @Transactional(readOnly = true)
    public List<PostDTO> getAllPostsByBoardName(String boardName, Pageable pageable) {
        List<Posts> posts = postRepository.findAllByBoardName(boardName, pageable);
        return posts.stream()
                .map(post -> convertToDto(post, determineDtoClass(post.getPostType())))
                .collect(Collectors.toList());
    }


    //게시글 번호로 글 가져오기
    @Transactional(readOnly = true)
    public PostDTO getPostById(String boardName, Long postId) {
        Posts post = postRepository.findByBoardNameandId(boardName, postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        return convertToDto(post, determineDtoClass(post.getPostType()));
    }
}
