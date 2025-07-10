package com.springboard.service;

import com.springboard.dto.post.PostCreateRequestDto;
import com.springboard.dto.post.PostCustomPageDto;
import com.springboard.dto.post.PostResponseDto;
import com.springboard.dto.post.PostUpdateRequestDto;
import com.springboard.entity.Post;
import com.springboard.entity.User;
import com.springboard.exception.custom.comment.CommentDeleteForbiddenException;
import com.springboard.exception.custom.comment.CommentUpdateForbiddenException;
import com.springboard.exception.custom.post.PostNotFoundException;
import com.springboard.exception.custom.user.UserNotFoundException;
import com.springboard.repository.PostLikeRepository;
import com.springboard.repository.PostRepository;
import com.springboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    //게시글 작성
    @Transactional
    public PostResponseDto createPost(PostCreateRequestDto dto, String username){
        User user = userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new UserNotFoundException());

        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setUser(user);

        postRepository.save(post);

        return new PostResponseDto(post);
    }

    //조회(단일)
    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long postId, String username) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new PostNotFoundException());
        if(username!=null){
            User user = userRepository.findByUsernameAndIsDeletedFalse(username)
                    .orElseThrow(() -> new UserNotFoundException());
            post.setLiked(postLikeRepository.existsByPostAndUser(post, user));
        }
        return new PostResponseDto(post);
    }

    //조회(페이징)
    @Transactional(readOnly = true)
    public PostCustomPageDto<PostResponseDto> getPosts(Long page) {
        int size = 10;
        Pageable pageable = PageRequest.of(page.intValue(), size, Sort.by(Sort.Direction.DESC, "id"));
        Page<PostResponseDto> result = postRepository.findAllByIsDeletedFalse(pageable)
                .map(PostResponseDto::new);
        return new PostCustomPageDto<>(result);
    }

    //게시글 수정
    @Transactional
    public PostResponseDto updatePost(Long postId, PostUpdateRequestDto dto, String username){
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new PostNotFoundException());

        if (!post.getUser().getUsername().equals(username)) {
            throw new CommentUpdateForbiddenException();
        }

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());

        return new PostResponseDto(post);
    }

    //게시글 삭제
    @Transactional
    public void deletePost(Long postId, String username){
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new PostNotFoundException());

        if (!post.getUser().getUsername().equals(username)) {
            throw new CommentDeleteForbiddenException();
        }
        post.setDeleted(true);
    }

    @Transactional
    public void postLikeCount(Long postId, Long postLikeCount){
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new PostNotFoundException());

        post.setPostLikeCount(postLikeCount);

        postRepository.save(post);
    }
}
