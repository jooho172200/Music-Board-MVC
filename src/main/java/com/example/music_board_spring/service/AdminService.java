package com.example.music_board_spring.service;

import com.example.music_board_spring.model.dto.ReportedDTO;
import com.example.music_board_spring.model.entity.Comments;
import com.example.music_board_spring.model.entity.Posts;
import com.example.music_board_spring.model.entity.Users;
import com.example.music_board_spring.repository.CommentRepository;
import com.example.music_board_spring.repository.PostRepository;
import com.example.music_board_spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;


    public List<ReportedDTO> getReportedPosts() {
        return postRepository.findByReportCount(10).stream()
                .map(this::convertToReportedDTO)
                .collect(Collectors.toList());
    }

    public List<ReportedDTO> getReportedComments() {
        return commentRepository.findByReportCount(10).stream()
                .map(this::convertToReportedDTO)
                .collect(Collectors.toList());
    }

    public Users activateUser(Long userId) {
        return userService.activateUser(userId);
    }

    public Users deactivateUser(Long userId) {
        return userService.deactivateUser(userId);
    }

    private ReportedDTO convertToReportedDTO(Object item){
        ReportedDTO reportedDTO = new ReportedDTO();

        if (item instanceof Posts) {
            Posts post = (Posts) item;
            reportedDTO.setId(post.getPostId());
            reportedDTO.setType("Post");
            reportedDTO.setContent(post.getContent());
            reportedDTO.setReportCount(post.getReportCount());
        } else if (item instanceof Comments) {
            Comments comment = (Comments) item;
            reportedDTO.setId(comment.getCommentId());
            reportedDTO.setType("Comment");
            reportedDTO.setContent(comment.getContent());
            reportedDTO.setReportCount(comment.getReportCount());
        }

        return reportedDTO;
    }

}
