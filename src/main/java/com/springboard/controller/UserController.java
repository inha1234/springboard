package com.springboard.controller;

import com.springboard.dto.post.PostResponseDto;
import com.springboard.dto.user.UserPasswordChangeDto;
import com.springboard.dto.user.UserProfileResponseDto;
import com.springboard.dto.user.UserProfileUpdateRequestDto;
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

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponseDto> getUserProfile(@PathVariable Long userId) {
        UserProfileResponseDto response = userService.getUserProfile(userId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/profile")
    public ResponseEntity<Void> updateProfile(@RequestBody @Valid UserProfileUpdateRequestDto dto,
                                              HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String username = jwtUtil.getUsernameFromAccessToken(token);
        userService.updateProfile(username, dto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid UserPasswordChangeDto dto,
                                               HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String username = jwtUtil.getUsernameFromAccessToken(token);
        userService.changePassword(username, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<String> withdrawUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String username = jwtUtil.getUsernameFromAccessToken(token);

        userService.withdraw(username);
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }
}
