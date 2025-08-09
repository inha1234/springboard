package com.springboard.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboard.exception.ErrorCode;
import com.springboard.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper om = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException ex) throws IOException {
        ErrorResponse body = ErrorResponse.of(ErrorCode.UNAUTHORIZED); // 토큰 만료/미인증 등
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(om.writeValueAsString(body));
    }
}
