package com.springboard.controller;

import com.springboard.dto.user.UserLoginResponseDto;
import com.springboard.entity.User;
import com.springboard.jwt.JwtUtil;
import com.springboard.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @PostMapping("/reissue")
    public ResponseEntity<UserLoginResponseDto> reissue(HttpServletRequest request) {
        String refreshToken = request.getHeader("Refresh-Token");
        String username = jwtUtil.getUsernameFromToken(refreshToken);


        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보가 유효하지 않습니다."));

        jwtUtil.validateToken(refreshToken);


        String newAccessToken = jwtUtil.generateToken(user.getUsername(), user.getNickname());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getNickname());

        return ResponseEntity.ok(new UserLoginResponseDto(newAccessToken, newRefreshToken));
    }
}