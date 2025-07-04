package com.springboard.controller;


import com.springboard.dto.comment.CommentCreateRequestDto;
import com.springboard.dto.comment.CommentResponseDto;
import com.springboard.dto.comment.CommentUpdateRequestDto;
import com.springboard.jwt.JwtUtil;
import com.springboard.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long postId,
            @RequestBody @Valid CommentCreateRequestDto dto,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String username = jwtUtil.getUsernameFromAccessToken(token);
        CommentResponseDto responseDto = commentService.createComment(postId, dto, username);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long postId){
        List<CommentResponseDto> comments = commentService.getComment(postId);
        return ResponseEntity.ok(comments);
    }


    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId,
                                                            @RequestBody @Valid CommentUpdateRequestDto dto,
                                                            HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String username = jwtUtil.getUsernameFromAccessToken(token);
        CommentResponseDto responseDto = commentService.updateComment(commentId, dto, username);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId,
                                                HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String username = jwtUtil.getUsernameFromAccessToken(token);
        commentService.deleteComment(commentId, username);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
}
