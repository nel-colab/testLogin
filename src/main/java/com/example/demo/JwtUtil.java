package com.example.demo;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.example.demo.model.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    private static final long EXPIRATION_TIME_MS = 3600000;
    private static final String SECRET = "miClaveSecretaMuySeguraYDeLongitudAdecuada123456";
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public String generateJwtToken(UserEntity user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME_MS);

        return Jwts.builder()
                .setSubject(user.getId())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .signWith(SECRET_KEY)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
