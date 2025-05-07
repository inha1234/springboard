package com.springboard.dto.comment;

import com.springboard.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private String author;
    private Long parentId;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.isDeleted() ? "삭제된 댓글입니다." : comment.getContent();
        this.author = comment.getUser().getNickname(); // 닉네임 표시
        this.parentId = comment.getParent() != null ? comment.getParent().getId() : null;
    }
}
