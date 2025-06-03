package com.springboard.repository;

import com.springboard.entity.Post;
import com.springboard.entity.PostLike;
import com.springboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByPostAndUser(Post post, User user);
    Optional<PostLike> findByPostAndUser(Post post, User user);
    long countByPostId(Long postId);
}