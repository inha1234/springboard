package com.springboard.service;

import com.springboard.dto.auth.AuthLoginRequestDto;
import com.springboard.dto.auth.AuthLoginResponseDto;
import com.springboard.entity.User;
import com.springboard.exception.custom.*;
import com.springboard.exception.custom.auth.PasswordMismatchException;
import com.springboard.exception.custom.auth.TokenMismatchException;
import com.springboard.exception.custom.auth.TokenNotFoundException;
import com.springboard.exception.custom.user.UserNotFoundException;
import com.springboard.jwt.JwtUtil;
import com.springboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final StringRedisTemplate redisTemplate;

    @Transactional(readOnly = true)
    public AuthLoginResponseDto login(AuthLoginRequestDto dto) {
        User user = userRepository.findByUsernameAndIsDeletedFalse(dto.getUsername())
                .orElseThrow(() -> new UserNotFoundException());

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new PasswordMismatchException();
        }
        String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getNickname());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getNickname());

        AuthLoginResponseDto responseDto = new AuthLoginResponseDto(accessToken, refreshToken);

        return responseDto;
    }

    @Transactional
    public AuthLoginResponseDto reissue(String username) {
        User user = userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new InvalidUserInformationException());

        String newAccessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getNickname());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getNickname());

        return new AuthLoginResponseDto(newAccessToken, newRefreshToken);
    }

    public void blacklistToken(String token, Long expiration) {
        if(expiration > 0){
            String redisKey = "BL:" + token;
            redisTemplate.opsForValue().set(redisKey, "blacklisted", expiration, TimeUnit.MILLISECONDS);
        }
    }

    public void validateHeaderTokens(String accessToken, String refreshToken) {
        if (accessToken == null || refreshToken == null) {
            throw new TokenNotFoundException();
        }
    }

    public void validateUsernameMatch(String accessUser, String refreshUser) {
        if (!accessUser.equals(refreshUser)) {
            throw new TokenMismatchException();
        }
    }

    public void blacklistTokens(String accessToken, String refreshToken) {
        if (accessToken != null) {
            accessToken = accessToken.replace("Bearer ", "");
            long accessTokenExpiration = jwtUtil.getAccessTokenExpiration(accessToken);
            blacklistToken(accessToken, accessTokenExpiration);
        }

        if (refreshToken != null) {
            refreshToken = refreshToken.replace("Bearer ", "");
            long refreshTokenExpiration = jwtUtil.getRefreshTokenExpiration(refreshToken);
            blacklistToken(refreshToken, refreshTokenExpiration);
        }
    }
}
