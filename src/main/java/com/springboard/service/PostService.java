package com.springboard.service;

import com.springboard.dto.post.PostCreateRequestDto;
import com.springboard.dto.post.PostCustomPageDto;
import com.springboard.dto.post.PostResponseDto;
import com.springboard.dto.post.PostUpdateRequestDto;
import com.springboard.entity.Post;
import com.springboard.entity.User;
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
                .orElseThrow(() -> new IllegalArgumentException("작성자 정보를 찾을 수 없습니다."));

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
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않거나 삭제된 게시글입니다."));
        if(username!=null){
            User user = userRepository.findByUsernameAndIsDeletedFalse(username)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
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
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않거나 삭제된 게시글입니다."));

        if (!post.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());

        return new PostResponseDto(post);
    }

    //게시글 삭제
    @Transactional
    public void deletePost(Long postId, String username){
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않거나 삭제된 게시글입니다."));

        if (!post.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }
        post.setDeleted(true);
    }

    @Transactional
    public void postLikeCount(Long postId, Long postLikeCount){
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않거나 삭제된 게시글입니다."));

        post.setPostLikeCount(postLikeCount);

        postRepository.save(post);
    }
}
