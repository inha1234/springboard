package com.springboard.jwt;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.expiration-time}")
    private final long expirationTime;
    private final Key key;
    public JwtUtil(@Value("${jwt.secret}") String secretKeyString,
            @Value("${jwt.expiration-time}") long expirationTime) {
        this.expirationTime = expirationTime;
        this.key = Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }


    //토큰 생성
    public String generateToken(String username, String nickname) {
        return Jwts.builder()
                .setSubject(username)
                .claim("nickname", nickname)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String username, String nickname){
        Long time = 60L;

        return Jwts.builder()
                .setSubject(username)
                .claim("nickname", nickname)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime + time))
                .signWith(key)
                .compact();
    }

    //토큰에서 유저 네임 추출
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody()
                .getSubject();
    }

    //토큰에서 유저 닉네임 추출
    public String getNicknameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody()
                .get("nickname", String.class);
    }
}
