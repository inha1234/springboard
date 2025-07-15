package com.springboard.jwt;

import com.springboard.exception.custom.user.UserNotFoundException;
import com.springboard.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, RedisTemplate<String, String> redisTemplate, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = resolveToken(request);

        if(accessToken == null){
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String username = jwtUtil.getUsernameFromAccessToken(accessToken);
            userRepository.findByUsernameAndIsDeletedFalse(username)
                    .orElseThrow(() -> new UserNotFoundException());
            accessToken = accessToken.replace("Bearer ", "");
            String blacklistKey = "BL:" + accessToken;
            String isBlacklisted = redisTemplate.opsForValue().get(blacklistKey);
            if (isBlacklisted != null) {
                throw new JwtException("블랙리스트 토큰입니다.");
            }

        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("AccessToken이 만료되었습니다.");
            return;
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("유효하지 않은 토큰입니다.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken (HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if (token == null || token.isBlank()) {
            return null;
        }

        // Bearer 붙어 있으면 잘라내고, 아니면 그냥 반환
        if (token.startsWith("Bearer ")) {
            return token.substring(7);
        } else {
            return token;
        }
    }
}
