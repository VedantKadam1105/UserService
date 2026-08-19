package com.neo.userservice.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class ServiceTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.service-token-expiration-ms:300000}")
    private long expirationMs;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateServiceToken() {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject("userservice")
                .claim("roles", "ROLE_SERVICE")
                .claim("tokenType", "service")
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }
}