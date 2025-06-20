package com.springboard.dto.post;

import com.springboard.entity.Post;
import lombok.Getter;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String authorUserNickname;
    private boolean liked;
    private long likeCount;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorUserNickname = post.getUser().getNickname();
        this.likeCount = post.getPostLikeCount() != null ? post.getPostLikeCount() : 0L;
    }
    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
