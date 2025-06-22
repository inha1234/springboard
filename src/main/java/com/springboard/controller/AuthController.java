package com.springboard.controller;

import com.springboard.dto.auth.AuthLoginRequestDto;
import com.springboard.dto.auth.AuthLoginResponseDto;
import com.springboard.entity.User;
import com.springboard.jwt.JwtUtil;
import com.springboard.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/reissue")
    public ResponseEntity<AuthLoginResponseDto> reissue(HttpServletRequest request) {
        String refreshToken = request.getHeader("Refresh-Token");
        String username = jwtUtil.getUsernameFromRefreshToken(refreshToken);


        User user = userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보가 유효하지 않습니다."));

        jwtUtil.validateToken(refreshToken);


        String newAccessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getNickname());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getNickname());

        return ResponseEntity.ok(new AuthLoginResponseDto(newAccessToken, newRefreshToken));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponseDto> login(@RequestBody @Valid AuthLoginRequestDto dto) {
        AuthLoginResponseDto token = authService.login(dto);
        return ResponseEntity.ok(token);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {

        return ResponseEntity.ok("로그아웃 되었습니다.");
    }
}