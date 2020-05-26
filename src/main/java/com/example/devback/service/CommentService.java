package com.example.devback.service;

import com.example.devback.dto.CommentsDto;
import com.example.devback.exceptions.PostNotFoundException;
import com.example.devback.mapper.CommentMapper;
import com.example.devback.model.Comment;
import com.example.devback.model.NotificationEmail;
import com.example.devback.model.Post;
import com.example.devback.repository.CommentRepository;
import com.example.devback.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {
    private static final String POST_URL = "";
    private final PostRepository postRepository;

    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        Comment comment = commentMapper.map(commentsDto, post);
        commentRepository.save(comment);
        String message = mailContentBuilder.build( " walid  posted a comment on your post." + POST_URL);
        sendCommentNotification(message);
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post).stream().map(commentMapper::mapToDto).collect(Collectors.toList());

    }
    private void sendCommentNotification(String message) {
        mailService.sendMail(new NotificationEmail( "zied  Commented on your post", "zied.elmiladi@gmail.com", message));
    }
}
