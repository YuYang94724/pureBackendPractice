package com.yuyang.pureBackendPractice;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.Optional;

public class PrintTest {
    private static final String key = "C874B58E53F97A2B15D0B5848RSJ5432GBSIJL09W5643BHG1E40924FCAF1C19BFA4484A1523704C0AB1F42";
    public static void main(String[] args) {
        // 生成 JWT
        String subject = "user123";
        Long ttlMillis = 3600000L; // 1小時
        String uuid = "abcd1234";
        String token = generate(subject, ttlMillis, uuid);
        System.out.println("Generated JWT: " + token);

        // 解析 JWT 取得主題（subject）值
        String retrievedSubject = retrieveSubject(token);
        System.out.println("Retrieved Subject: " + retrievedSubject);
    }

    public static String generate(String subject, Long ttlMillis, String uuid) {
        return Optional
                .of(new Date())
                .map(v -> Jwts
                        .builder()
                        .setIssuer("pure-backend-practice")
                        .setSubject(subject)
                        .setAudience(null)
                        .setExpiration(new Date(System.currentTimeMillis() + ttlMillis))
                        .setNotBefore(v)
                        .setIssuedAt(v)
                        .setId(uuid)
                        .signWith(Keys.hmacShaKeyFor(key.getBytes()), SignatureAlgorithm.HS512)
                        .compact()
                )
                .get();
    }

    public static String retrieveSubject(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(key.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
