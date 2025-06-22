package com.springboard.jwt;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.expiration-time}")
    private final long expirationTime;
    private final Key accessKey;
    private final Key refreshKey;
    public JwtUtil(@Value("${jwt.secretAccessKey}") String secretAccessKeyString,
                   @Value("${jwt.secretRefreshKey}") String secretRefreshKeyString,
            @Value("${jwt.expiration-time}") long expirationTime) {
        this.expirationTime = expirationTime;
        this.accessKey = Keys.hmacShaKeyFor(secretAccessKeyString.getBytes());
        this.refreshKey = Keys.hmacShaKeyFor(secretRefreshKeyString.getBytes());
    }


    //토큰 생성
    public String generateAccessToken(String username, String nickname) {
        return Jwts.builder()
                .setSubject(username)
                .claim("nickname", nickname)
                .claim("type", "access")
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(accessKey)
                .compact();
    }

    public String generateRefreshToken(String username, String nickname){
        Long time = 30*60*1000L;

        return Jwts.builder()
                .setSubject(username)
                .claim("nickname", nickname)
                .claim("type", "refresh")
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime + time))
                .signWith(refreshKey)
                .compact();
    }

    //토큰에서 유저 네임 추출
    public String getUsernameFromAccessToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(accessKey)
                .build()
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody()
                .getSubject();
    }

    public String getUsernameFromRefreshToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(refreshKey)
                .build()
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody()
                .getSubject();
    }

    //토큰에서 유저 닉네임 추출
    public String getNicknameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(accessKey)
                .build()
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody()
                .get("nickname", String.class);
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(accessKey)
                    .build()
                    .parseClaimsJws(token.replace("Bearer ", ""));
        } catch (JwtException e) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }
}
