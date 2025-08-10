package com.springboard.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboard.jwt.JwtAuthenticationFilter;
import com.springboard.jwt.JwtUtil;
import com.springboard.security.CustomAccessDeniedHandler;
import com.springboard.security.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
//@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtUtil jwtUtil,
                                           CustomAuthenticationEntryPoint entryPoint,
                                           CustomAccessDeniedHandler deniedHandler) throws Exception {
        http
                .csrf().disable() // csrf 비활성화 (API 개발용)
                .headers().frameOptions().disable() // H2 콘솔 프레임 허용
                .and()
                .cors()
                .and()
                .authorizeHttpRequests(auth -> auth
                        // CORS preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 완전 공개
                        .requestMatchers(
                                "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**",
                                "/h2-console/**", "/error", "/health"
                        ).permitAll()

                        // 로그인/회원가입/재발급 등 인증 진입점 (컨트롤러: /api/auth/..)
                        .requestMatchers("/api/auth/**").permitAll()

                        // 읽기 전용 공개 API (컨트롤러: /api/posts, /api/posts/{id}, /api/posts/{id}/comments..)
                        .requestMatchers(HttpMethod.GET,
                                "/api/posts", "/api/posts/**",
                                "/api/posts/*/comments/**"   // {postId}/comments 하위 GET 허용
                        ).permitAll()

                        // 그 외는 인증 필요
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(entryPoint)         // 401
                        .accessDeniedHandler(deniedHandler)           // 403
                )
                .formLogin().disable() // 기본 로그인 폼 비활성화 (원하면 유지 가능)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080")); // 허용할 출처 (스웨거가 실행되는 곳)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // 허용할 메서드
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // 허용할 헤더

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 CORS 설정

        return source;
    }
    @Bean
    public CustomAuthenticationEntryPoint authenticationEntryPoint(ObjectMapper om) {
        return new CustomAuthenticationEntryPoint(om);
    }

    @Bean
    public CustomAccessDeniedHandler accessDeniedHandler(ObjectMapper om) {
        return new CustomAccessDeniedHandler(om);
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
