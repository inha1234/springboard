package com.springboard.service;

import com.springboard.dto.auth.AuthLoginRequestDto;
import com.springboard.dto.auth.AuthLoginResponseDto;
import com.springboard.entity.User;
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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getNickname());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getNickname());

        AuthLoginResponseDto responseDto = new AuthLoginResponseDto(accessToken, refreshToken);

        return responseDto;
    }

    @Transactional
    public AuthLoginResponseDto reissue(String username) {
        User user = userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보가 유효하지 않습니다."));

        String newAccessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getNickname());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getNickname());

        return new AuthLoginResponseDto(newAccessToken, newRefreshToken);
    }

    public void blacklistToken(String token, long expirationMillis) {
        redisTemplate.opsForValue().set(token, "blacklisted", expirationMillis, TimeUnit.MILLISECONDS);
    }


    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }
}
