package com.springboard.repository;

import com.springboard.entity.Comment;
import com.springboard.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
    boolean existsByIdAndUser_Username(Long commentId, String username);
}