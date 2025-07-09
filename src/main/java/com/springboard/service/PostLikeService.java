package com.springboard.service;

import com.springboard.entity.Post;
import com.springboard.entity.PostLike;
import com.springboard.entity.User;
import com.springboard.exception.custom.post.PostNotFoundException;
import com.springboard.exception.custom.postlike.AlreadyLikedException;
import com.springboard.exception.custom.postlike.LikeNotFoundException;
import com.springboard.exception.custom.user.UserNotFoundException;
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
                .orElseThrow(() -> new UserNotFoundException());
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new PostNotFoundException());

        boolean alreadyLiked = postLikeRepository.existsByPostAndUser(post, user);
        if (alreadyLiked) {
            throw new AlreadyLikedException();
        }

        PostLike like = new PostLike(post, user);

        postLikeRepository.save(like);
        postService.postLikeCount(postId, getLikeCount(postId));
    }

    @Transactional
    public void unLikePost(Long postId, String username) {
        User user = userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new UserNotFoundException());
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new PostNotFoundException());

        PostLike postLike = postLikeRepository.findByPostAndUser(post, user)
                .orElseThrow(() -> new LikeNotFoundException());

        postLikeRepository.delete(postLike);
    }


    public long getLikeCount(Long postId){
        return postLikeRepository.countByPostId(postId);
    }
}
