package com.springboard.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisTestRunner implements CommandLineRunner {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void run(String... args) {
        try {
            redisTemplate.opsForValue().set("testKey", "hello redis");
            String value = redisTemplate.opsForValue().get("testKey");
            System.out.println("🟥 Redis 연결 성공! 값: " + value);
        } catch (Exception e) {
            System.out.println("❌ Redis 연결 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
}