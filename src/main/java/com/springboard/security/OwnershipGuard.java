package com.springboard.security;

import com.springboard.repository.CommentRepository;
import com.springboard.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OwnershipGuard {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public boolean postOwner(Long postId, Authentication authentication) {
        String username = ((AuthUser) authentication.getPrincipal()).getUsername();
        return postRepository.existsByIdAndUser_Username(postId, username);
    }

    public boolean commentOwner(Long commentId, Authentication authentication) {
        String username = ((AuthUser) authentication.getPrincipal()).getUsername();
        return commentRepository.existsByIdAndUser_Username(commentId, username);
    }
}
