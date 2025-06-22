package com.springboard.dto.user;

import com.springboard.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSummaryDto {
    private Long userId;
    private String nickname;

    public UserSummaryDto(User user) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
    }
}
