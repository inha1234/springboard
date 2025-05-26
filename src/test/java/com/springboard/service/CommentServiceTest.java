package com.springboard.service;

import com.springboard.dto.comment.CommentRequestDto;
import com.springboard.dto.comment.CommentResponseDto;
import com.springboard.entity.Comment;
import com.springboard.entity.Post;
import com.springboard.entity.User;
import com.springboard.repository.CommentRepository;
import com.springboard.repository.PostRepository;
import com.springboard.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
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

    static User user;
    static Post post;
    static Comment comment;
    @BeforeAll
    static void beforeAll(){
        user = new User();
        user.setUsername("testUser");
        user.setNickname("testUser");
        user.setPassword("1234");

        post = new Post();
        post.setTitle("테스트 제목");
        post.setContent("테스트 내용");
        post.setUser(user);

        comment = new Comment();
        comment.setContent("테스트 댓글");
        comment.setPost(post);
        comment.setUser(user);
    }
    @Test
    void 댓글_작성_성공_일반_댓글(){
        CommentRequestDto dto = new CommentRequestDto();
        dto.setContent("댓글 내용");

        given(postRepository.findById(1L)).willReturn(Optional.of(post));
        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));
//        given(commentRepository.save(any(Comment.class))).willAnswer(invocation -> {
//            Comment saved = invocation.getArgument(0);
//            return saved;
//        }); //필요없을 거 같으니깐 주석처리

        CommentResponseDto responseDto = commentService.createComment(1L, dto, user.getUsername());

        assertThat(responseDto.getContent()).isEqualTo("댓글 내용");
        assertThat(responseDto.getParentId()).isNull();
    }

    @Test
    void 댓글_작성_성공_대댓글(){
        Long parentCommentId = comment.getId();

        CommentRequestDto dto = new CommentRequestDto();
        dto.setContent("대댓글 내용");
        dto.setParentId(parentCommentId);

        given(postRepository.findById(1L)).willReturn(Optional.of(post));
        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));
        given(commentRepository.findById(parentCommentId)).willReturn(Optional.of(comment));

        CommentResponseDto responseDto = commentService.createComment(1L, dto, user.getUsername());

        assertThat(responseDto.getContent()).isEqualTo("대댓글 내용");
        assertThat(responseDto.getParentId()).isEqualTo(parentCommentId);
    }

}

