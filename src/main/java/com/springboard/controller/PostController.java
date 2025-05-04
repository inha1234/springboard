package com.springboard.controller;

import com.springboard.dto.PostRequestDto;
import com.springboard.dto.PostResponseDto;
import com.springboard.service.PostService;
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

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody @Valid PostRequestDto dto,
            @RequestHeader("Authorization") String token) {
        PostResponseDto responseDto = postService.createPost(dto, token);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        PostResponseDto responseDto = postService.getPost(postId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> getPosts(@RequestParam(defaultValue = "0") Long page) {
        Page<PostResponseDto> response = postService.getPosts(page);
        return ResponseEntity.ok(response);
    }
}
