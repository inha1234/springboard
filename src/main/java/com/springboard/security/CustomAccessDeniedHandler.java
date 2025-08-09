package com.springboard.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboard.exception.ErrorCode;
import com.springboard.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper om = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException ex) throws IOException {
        ErrorResponse body = ErrorResponse.of(ErrorCode.FORBIDDEN); // 프로젝트 코드에 맞게
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(om.writeValueAsString(body));
    }
}
