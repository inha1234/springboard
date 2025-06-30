package com.springboard.controller;

import com.springboard.dto.auth.AuthLoginRequestDto;
import com.springboard.dto.auth.AuthLoginResponseDto;
import com.springboard.jwt.JwtUtil;
import com.springboard.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtUtil jwtUtil;
    private final AuthService authService;

    @PostMapping("/reissue")
    public ResponseEntity<AuthLoginResponseDto> reissue(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        String refreshToken = request.getHeader("RefreshToken");

        authService.validateHeaderTokens(accessToken, refreshToken);

        String accessTokenUsername = jwtUtil.getUsernameFromAccessToken(accessToken);
        String refreshTokenUsername = jwtUtil.getUsernameFromRefreshToken(refreshToken);

        authService.validateUsernameMatch(accessTokenUsername, refreshTokenUsername);

        AuthLoginResponseDto token = authService.reissue(refreshTokenUsername);

        authService.blacklistTokens(accessToken, refreshToken);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponseDto> login(@RequestBody @Valid AuthLoginRequestDto dto) {
        AuthLoginResponseDto token = authService.login(dto);
        return ResponseEntity.ok(token);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        jwtUtil.getUsernameFromAccessToken(token);

        token = token.replace("Bearer ", "");
        long expiration = jwtUtil.getAccessTokenExpiration(token);

        authService.blacklistToken(token, expiration);

        return ResponseEntity.ok("로그아웃 되었습니다.");
    }
}