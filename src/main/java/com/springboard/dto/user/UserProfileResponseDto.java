package com.springboard.dto.user;

import com.springboard.entity.User;
import lombok.Getter;

@Getter
public class UserProfileResponseDto {
    private String nickname;

    public UserProfileResponseDto(User user) {
        this.nickname = user.getNickname();

    }
}
