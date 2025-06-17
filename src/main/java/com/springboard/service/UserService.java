package com.springboard.service;

import com.springboard.dto.user.UserLoginRequestDto;
import com.springboard.dto.user.UserLoginResponseDto;
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

    @Transactional(readOnly = true)
    public UserLoginResponseDto login(UserLoginRequestDto dto) {
        User user = userRepository.findByUsernameAndIsDeletedFalse(dto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        String accessToken = jwtUtil.generateToken(user.getUsername(), user.getNickname());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getNickname());

        UserLoginResponseDto responseDto = new UserLoginResponseDto(accessToken, refreshToken);

        return responseDto;
    }

    @Transactional
    public void withdraw(String username) {
        User user = userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setDeleted(true);
    }
}