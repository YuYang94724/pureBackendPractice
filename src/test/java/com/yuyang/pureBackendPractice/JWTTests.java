package com.yuyang.pureBackendPractice;

import com.yuyang.pureBackendPractice.comfig.securtiy.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class JWTTests {
    @Resource
    private JwtTokenService jwtTokenService;

    @Test
    public void test(){
        // 生成 JWT
        String subject = "user123";
        Long ttlMillis = 3600000L; // 1小時
        String uuid = UUID.randomUUID().toString();
        String token = jwtTokenService.generate(subject, ttlMillis, uuid);
        // 解析 JWT 取得主題（subject）值
        String encode = jwtTokenService.retrieveSubject(token);
        assertEquals("user123", encode);
    }

}
