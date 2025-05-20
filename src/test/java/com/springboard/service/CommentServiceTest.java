package com.springboard.service;

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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


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

        Comment comment = new Comment();
        comment.setContent("댓글 내용");
        comment.setPost(post);
        comment.setUser(user);

        Mockito.when(commentRepository.findAll()).thenReturn(List.of(comment));

        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(1);
        assertThat(comments.get(0).getPost().getId()).isEqualTo(post.getId());

    }

}

