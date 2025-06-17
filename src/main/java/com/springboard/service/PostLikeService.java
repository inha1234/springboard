package com.springboard.service;

import com.springboard.entity.Post;
import com.springboard.entity.PostLike;
import com.springboard.entity.User;
import com.springboard.repository.PostLikeRepository;
import com.springboard.repository.PostRepository;
import com.springboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostService postService;

    @Transactional
    public void likePost(Long postId, String username) {
        User user = userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않거나 삭제된 게시글입니다."));

        boolean alreadyLiked = postLikeRepository.existsByPostAndUser(post, user);
        if (alreadyLiked) {
            throw new IllegalStateException("이미 좋아요를 눌렀습니다.");
        }

        PostLike like = new PostLike(post, user);

        postLikeRepository.save(like);
        postService.postLikeCount(postId, getLikeCount(postId));
    }

    @Transactional
    public void unLikePost(Long postId, String username) {
        User user = userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않거나 삭제된 게시글입니다."));

        PostLike postLike = postLikeRepository.findByPostAndUser(post, user)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글에 대한 좋아요 기록이 없습니다."));

        postLikeRepository.delete(postLike);
    }


    public long getLikeCount(Long postId){
        return postLikeRepository.countByPostId(postId);
    }
}
