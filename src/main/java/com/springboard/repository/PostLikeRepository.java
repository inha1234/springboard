package com.springboard.repository;

import com.springboard.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<Post, Long> {
}