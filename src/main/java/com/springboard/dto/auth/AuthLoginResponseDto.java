package com.springboard.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthLoginResponseDto {
    private String accessToken;
    private String refreshToken;
}
