package com.example.music_board_spring.service;


import com.example.music_board_spring.exception.UserNotFoundException;
import com.example.music_board_spring.model.dto.MarketPostDTO;
import com.example.music_board_spring.model.dto.PostDTO;
import com.example.music_board_spring.model.entity.Boards;
import com.example.music_board_spring.model.entity.Posts;
import com.example.music_board_spring.model.entity.Users;
import com.example.music_board_spring.repository.BoardRepository;
import com.example.music_board_spring.repository.PostRepository;
import com.example.music_board_spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    //글 게시
    @Transactional
    public  <T extends PostDTO> T createPost(T postDTO, MultipartFile file){
        Posts post = convertToEntity(postDTO);

        // 사용자 정보 설정
        Users user = userRepository.findById(postDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 업습니다. "));
        post.setUser(user);

        // 게시판 정보 설정
        Boards board = boardRepository.findByBoardName(postDTO.getPostType())
                .orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다. "));
        post.setBoard(board);

        if (file != null && !file.isEmpty()) {
            String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files";
            String uuid = UUID.randomUUID().toString();
            String originalFilename = file.getOriginalFilename();
            String fileName = uuid + "_" + originalFilename;
            String filePath = "/files/" + fileName;

            try {
                File saveFile = new File(projectPath, fileName);

                //디렉토리가 없다면 생성
                if (!saveFile.getParentFile().exists()) {
                    saveFile.getParentFile().mkdirs();
                }
                file.transferTo(saveFile);

                post.setFilename(originalFilename);
                post.setFilepath(filePath);

            } catch (IOException e) {
                throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
            }
        }

        Posts savedPost = postRepository.save(post);
        return (T) convertToDto(savedPost, postDTO.getClass());
    }

    //글 삭제
    @PreAuthorize("hasRole('ADMIN') or #post.user.userId == principal.userId")
    @Transactional
    public void deletePost(String boardName, Long postId, Authentication authentication){
        Posts post = postRepository.findByBoardNameandId(boardName, postId)
                .orElseThrow(()-> new RuntimeException("게시글을 찾을 수 없습니다. "));

        Users currentUser = (Users) authentication.getPrincipal();

        postRepository.delete(post);
    }

    //글 수정
    @PreAuthorize("hasRole('ADMIN') or #post.user.userId == principal.userId")
    @Transactional
    public <T extends PostDTO> T updatePost(Long postId, T postDTO, Authentication authentication){
        Posts post = postRepository.findById(postId)
                .orElseThrow(()-> new RuntimeException("게시글을 찾을 수 없습니다. "));

        Users currentUser = (Users) authentication.getPrincipal();

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
            postDto.setFilename(post.getFilename());
            postDto.setFilepath(post.getFilepath());
            postDto.setUsername(post.getUser().getUsername());

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
    public List<? extends PostDTO> getPostsByUserId(String boardType, Long userId, Pageable pageable) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저입니다."));
        List<Posts> posts = postRepository.findByUserId(user.getUserId(), pageable);
        return posts.stream()
                .map(post -> convertToDto(post, determineDtoClass(post.getPostType())))
                .collect(Collectors.toList());
    }

    //전체 게시글 반환
    @Transactional(readOnly = true)
    public List<? extends PostDTO> getAllPostsByBoardName(String boardName, Pageable pageable) {
        List<Posts> posts = postRepository.findAllByBoardName(boardName, pageable);
        return posts.stream()
                .map(post -> convertToDto(post, determineDtoClass(post.getPostType())))
                .collect(Collectors.toList());
    }


    //게시글 번호로 글 가져오기
    @Transactional(readOnly = true)
    public <T extends PostDTO> T getPostById(String boardName, Long postId) {
        Posts post = postRepository.findByBoardNameandId(boardName, postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 게시글의 postType에 따라 적절한 DTO 클래스를 결정하여 반환
        Class<? extends PostDTO> dtoClass = determineDtoClass(post.getPostType());
        return (T) convertToDto(post, dtoClass);
    }
}
