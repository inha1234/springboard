package com.springboard.service;

import com.springboard.dto.comment.CommentRequestDto;
import com.springboard.dto.comment.CommentResponseDto;
import com.springboard.entity.Comment;
import com.springboard.entity.Post;
import com.springboard.entity.User;
import com.springboard.repository.CommentRepository;
import com.springboard.repository.PostRepository;
import com.springboard.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @InjectMocks
    private CommentService commentService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    void 댓글_작성_성공_일반_댓글(){
        User user = new User();
        user.setUsername("testUser");
        user.setNickname("testUser");
        user.setPassword("1234");


        Post post = new Post();
        post.setTitle("테스트 제목");
        post.setContent("테스트 내용");
        post.setUser(user);

        CommentRequestDto dto = new CommentRequestDto();
        dto.setContent("댓글 내용");

        given(postRepository.findById(1L)).willReturn(Optional.of(post));
        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));
        given(commentRepository.save(any(Comment.class))).willAnswer(invocation -> {
            Comment saved = invocation.getArgument(0);
            return saved;
        });

        CommentResponseDto responseDto = commentService.createComment(1L, dto, user.getUsername());

        assertThat(responseDto.getContent()).isEqualTo("댓글 내용");
        assertThat(responseDto.getParentId()).isNull();
    }

}

