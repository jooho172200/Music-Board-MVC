package com.example.music_board_spring.service;

import com.example.music_board_spring.model.dto.CommentDTO;
import com.example.music_board_spring.model.entity.Comments;
import com.example.music_board_spring.model.entity.Posts;
import com.example.music_board_spring.model.entity.Users;
import com.example.music_board_spring.repository.CommentRepository;
import com.example.music_board_spring.repository.PostRepository;
import com.example.music_board_spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //댓글 게시
    public CommentDTO createComment(Long postId, Long userId, String content){
        Posts post = postRepository.findById(postId)
                .orElseThrow(()-> new RuntimeException("게시글을 찾을 수 없습니다."));

        Users user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("유저를 찾을 수 없습니다."));

        Comments comment = new Comments();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        comment.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        Comments savedComment = commentRepository.save(comment);
        return convertToDto(savedComment);
    }

    //댓글 삭제
    public void deleteComment(Long commentId){
        Comments comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new RuntimeException("댓글을 찾을 수 없습니다."));

        commentRepository.delete(comment);
    }

    //댓글 수정
    public CommentDTO updateComment(Long commentId, CommentDTO commentDTO){
        Comments comment = commentRepository.findById(commentId)
                .orElseThrow(()->new RuntimeException("댓글을 찾을 수 없습니다."));

        updateFromDTO(comment, commentDTO);

        Comments updatedComment = commentRepository.save(comment);

        return convertToDto(updatedComment);

    }

    private void updateFromDTO(Comments comment, CommentDTO commentDTO){
        comment.setContent(commentDTO.getContent());
        comment.setUpdatedAt(commentDTO.getUpdatedAt());
    }

    // Comment를 DTO로 변환
    private CommentDTO convertToDto(Comments comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCommentId(comment.getCommentId());
        commentDTO.setPostId(comment.getPost().getPostId());
        commentDTO.setUserId(comment.getUser().getUserId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setUpdatedAt(comment.getUpdatedAt());
        return commentDTO;
    }

    //DTO를 Comment로 변환

    //모든 댓글 불러오기
    public List<CommentDTO> getAllComments(Long postId, Pageable pageable){
        List<Comments> comments = commentRepository.findByPostId(postId, pageable);
        return comments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    //유저가 작성한 댓글 불러오기
    public List<CommentDTO> getUserComments(Long userId,Pageable pageable){
        Users user = userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("유저를 찾을 수 없습니다."));

        List<Comments> comments = commentRepository.findByUserId(user.getUserId(), pageable);

        return comments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


}
