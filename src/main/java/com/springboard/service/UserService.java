package com.springboard.service;

import com.springboard.dto.user.UserPasswordChangeDto;
import com.springboard.dto.user.UserProfileResponseDto;
import com.springboard.dto.user.UserProfileUpdateRequestDto;
import com.springboard.dto.user.UserSignupDto;
import com.springboard.entity.User;
import com.springboard.exception.custom.user.DuplicateNicknameException;
import com.springboard.exception.custom.user.UserNotFoundException;
import com.springboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void signup(UserSignupDto dto) {

        userRepository.findByNicknameAndIsDeletedFalse(dto.getNickname())
                .ifPresent(user -> {
                    throw new DuplicateNicknameException();
                });

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(dto.getNickname());

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserProfileResponseDto getUserProfile(Long userId) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new UserNotFoundException());

        return new UserProfileResponseDto(user);
    }

    @Transactional
    public void updateProfile(String username, UserProfileUpdateRequestDto dto){
        userRepository.findByNicknameAndIsDeletedFalse(dto.getNickname())
                .ifPresent(otherUser -> {
                    throw new DuplicateNicknameException();
                });

        User user = userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new UserNotFoundException());

        user.setNickname(dto.getNickname());

    }

    @Transactional
    public void changePassword(String username, UserPasswordChangeDto dto){
        User user = userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new UserNotFoundException());

        String encoded = passwordEncoder.encode(dto.getPassword());
        user.setPassword(encoded);

    }

    @Transactional
    public void withdraw(String username) {
        User user = userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new UserNotFoundException());

        user.setDeleted(true);
    }
}