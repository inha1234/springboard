package com.springboard.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public PostLike(Post post, User user) {
        this.post = post;
        this.user = user;
    }

}
