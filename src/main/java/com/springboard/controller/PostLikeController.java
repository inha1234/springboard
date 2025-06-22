package com.springboard.controller;

import com.springboard.jwt.JwtUtil;
import com.springboard.service.PostLikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/like")
public class PostLikeController {
    private final PostLikeService postLikeService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> likePost(@PathVariable Long postId,
                                      HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String username = jwtUtil.getUsernameFromAccessToken(token);

        postLikeService.likePost(postId, username);
        return ResponseEntity.ok("좋아요가 등록되었습니다.");
    }

    @DeleteMapping
    public ResponseEntity<?> unlikePost(@PathVariable Long postId,
                                        HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String username = jwtUtil.getUsernameFromAccessToken(token);

        postLikeService.unLikePost(postId, username);
        return ResponseEntity.ok("좋아요가 취소되었습니다.");
    }
}
