package com.springboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // csrf 비활성화 (API 개발용)
                .headers().frameOptions().disable() // H2 콘솔 프레임 허용
                .and()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/h2-console/**"
                ).permitAll()
                .anyRequest().permitAll() // 임시: 모든 경로 허용 (나중에 수정 가능)
                .and()
                .formLogin().disable(); // 기본 로그인 폼 비활성화 (원하면 유지 가능)

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
