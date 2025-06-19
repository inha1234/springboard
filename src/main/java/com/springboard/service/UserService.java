package com.springboard.service;

import com.springboard.dto.auth.AuthLoginRequestDto;
import com.springboard.dto.auth.AuthLoginResponseDto;
import com.springboard.dto.user.UserSignupDto;
import com.springboard.jwt.JwtUtil;
import com.springboard.repository.UserRepository;
import com.springboard.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public void signup(UserSignupDto dto) {

        userRepository.findByNicknameAndIsDeletedFalse(dto.getNickname())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
                });

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(dto.getNickname());

        userRepository.save(user);
    }

    @Transactional
    public void withdraw(String username) {
        User user = userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setDeleted(true);
    }
}