package com.springboard.service;

import com.springboard.dto.PostRequestDto;
import com.springboard.dto.PostResponseDto;
import com.springboard.entity.Post;
import com.springboard.entity.User;
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

    public PostResponseDto createPost(PostRequestDto dto, String token){
        // TODO: JWT 토큰에서 username 파싱하는 로직 추가 필요
        String username = "user"; // 지금은 테스트용으로 임시 user 넣기
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("작성자 정보를 찾을 수 없습니다."));

        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setUser(user);

        postRepository.save(post);

        return new PostResponseDto(post);
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        return new PostResponseDto(post);
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDto> getPosts(Long page) {
        int size = 10;
        Pageable pageable = PageRequest.of(page.intValue(), size, Sort.by(Sort.Direction.DESC, "id"));
        return postRepository.findAll(pageable)
                .map(PostResponseDto::new);
    }
}
