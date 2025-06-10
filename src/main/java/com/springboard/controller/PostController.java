package com.springboard.controller;

import com.springboard.dto.post.PostCreateRequestDto;
import com.springboard.dto.post.PostResponseDto;
import com.springboard.dto.post.PostUpdateRequestDto;
import com.springboard.jwt.JwtUtil;
import com.springboard.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody @Valid PostCreateRequestDto dto,
                                                      HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String username = jwtUtil.getUsernameFromToken(token);
        PostResponseDto responseDto = postService.createPost(dto, username);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId,
                                                   HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String username = null;
        if(token!=null){
            username = jwtUtil.getUsernameFromToken(token);
        }
        PostResponseDto responseDto = postService.getPost(postId, username);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> getPosts(@RequestParam(defaultValue = "0") Long page) {
        Page<PostResponseDto> response = postService.getPosts(page);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId,
                                                      @RequestBody @Valid PostUpdateRequestDto dto,
                                                      HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String username = jwtUtil.getUsernameFromToken(token);
        PostResponseDto responseDto = postService.updatePost(postId, dto, username);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId,
                                             HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String username = jwtUtil.getUsernameFromToken(token);
        postService.deletePost(postId, username);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }
}
