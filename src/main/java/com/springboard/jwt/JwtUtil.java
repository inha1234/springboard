package com.springboard.jwt;


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
    private final Key key;
    public JwtUtil(@Value("${jwt.secret}") String secretKeyString,
            @Value("${jwt.expiration-time}") long expirationTime) {
        this.expirationTime = expirationTime;
        this.key = Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }


    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }
}
