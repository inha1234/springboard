package com.springboard.service;

import com.springboard.controller.CommentController;
import com.springboard.dto.comment.CommentCreateRequestDto;
import com.springboard.dto.comment.CommentResponseDto;
import com.springboard.dto.comment.CommentUpdateRequestDto;
import com.springboard.entity.Comment;
import com.springboard.entity.Post;
import com.springboard.entity.User;
import com.springboard.repository.CommentRepository;
import com.springboard.repository.PostRepository;
import com.springboard.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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
    @DisplayName("댓글_작성_성공_일반_댓글")
    void commentTest(){
        CommentCreateRequestDto dto = new CommentCreateRequestDto();
        dto.setContent("댓글 내용");
        Long postId = 1L;

        given(postRepository.findById(postId)).willReturn(Optional.of(post));
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
    @DisplayName("댓글_작성_성공_대댓글")
    void replayCommentTest(){
        Long commentId = 10L;
        ReflectionTestUtils.setField(comment, "id", commentId);
        Long parentCommentId = comment.getId();
        Long postId = 1L;

        CommentCreateRequestDto dto = new CommentCreateRequestDto();
        dto.setContent("대댓글 내용");
        dto.setParentId(parentCommentId);

        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));
        given(commentRepository.findById(parentCommentId)).willReturn(Optional.of(comment));

        CommentResponseDto responseDto = commentService.createComment(postId, dto, user.getUsername());

        assertThat(responseDto.getContent()).isEqualTo("대댓글 내용");
        assertThat(responseDto.getParentId()).isEqualTo(parentCommentId);
    }

    @Test
    @DisplayName("댓글_작성_실패_게시글_없음")
    void createComment_whenPostDoesNotExist(){
        Long postId = 1L;

        CommentCreateRequestDto dto = new CommentCreateRequestDto();
        dto.setContent("댓글 내용");

        given(postRepository.findById(postId)).willReturn(Optional.empty());
        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));

        assertThatThrownBy(() -> commentService.createComment(postId, dto, user.getUsername()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게시글이 존재하지 않습니다.");

    }

    @Test
    @DisplayName("댓글_수정_성공")
    void updateCommentTest(){
        Long commentId = 1L;
        CommentUpdateRequestDto dto = new CommentUpdateRequestDto();
        dto.setContent("수정된 댓글 내용");

        ReflectionTestUtils.setField(comment, "id", commentId);

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        CommentResponseDto responseDto = commentService.updateComment(commentId, dto, user.getUsername());

        assertThat(responseDto.getContent()).isEqualTo("수정된 댓글 내용");
    }

    @Test
    @DisplayName("댓글_수정_실패 - 댓글이 존재하지 않음")
    void updateCommentFalseTest1(){
        Long commentId = 1L;
        CommentUpdateRequestDto dto = new CommentUpdateRequestDto();
        dto.setContent("수정된 댓글 내용");

        given(commentRepository.findById(commentId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.updateComment(commentId, dto, user.getUsername()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("댓글이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("댓글_수정_실패 - 작성자 불일치")
    void updateCommentFalseTest2(){
        Long commentId = 1L;
        CommentUpdateRequestDto dto = new CommentUpdateRequestDto();
        dto.setContent("수정된 댓글 내용");

        User otherUser = new User();
        otherUser.setUsername("testUser2");
        otherUser.setNickname("testUser2");
        otherUser.setPassword("1234");

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        assertThatThrownBy(() -> commentService.updateComment(commentId, dto, otherUser.getUsername()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("작성자만 댓글을 수정할 수 있습니다.");
    }

    @Test
    @DisplayName("댓글 수정 실패 - 삭제된 댓글")
    void updateCommentFalseTest3(){
        Long commentId = 1L;
        CommentUpdateRequestDto dto = new CommentUpdateRequestDto();
        dto.setContent("수정된 댓글 내용");

        comment.setDeleted(true);

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        assertThatThrownBy(() -> commentService.updateComment(commentId, dto, user.getUsername()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("삭제된 댓글은 수정할 수 없습니다.");
    }
}

