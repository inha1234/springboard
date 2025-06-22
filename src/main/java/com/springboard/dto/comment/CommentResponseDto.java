package com.springboard.dto.comment;

import com.springboard.dto.user.UserSummaryDto;
import com.springboard.entity.Comment;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private UserSummaryDto author;
    private Long parentId;
    private List<CommentResponseDto> children = new ArrayList<>();

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.isDeleted() ? "삭제된 댓글입니다." : comment.getContent();
        this.author = new UserSummaryDto(comment.getUser()); // 닉네임 표시
        this.parentId = comment.getParent() != null ? comment.getParent().getId() : null;
    }

    public void addChild(CommentResponseDto child){
        this.children.add(child);
    }
}
