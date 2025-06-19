package com.springboard.controller;

import com.springboard.dto.auth.AuthLoginRequestDto;
import com.springboard.dto.auth.AuthLoginResponseDto;
import com.springboard.dto.user.UserSignupDto;
import com.springboard.jwt.JwtUtil;
import com.springboard.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid UserSignupDto dto) {
        userService.signup(dto);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<String> withdrawUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String username = jwtUtil.getUsernameFromToken(token);

        userService.withdraw(username);
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }
}
