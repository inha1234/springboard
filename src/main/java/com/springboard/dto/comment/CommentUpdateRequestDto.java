package com.springboard.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateRequestDto {
    @NotBlank(message = "내용은 필수입니다.")
    private String content;
}
