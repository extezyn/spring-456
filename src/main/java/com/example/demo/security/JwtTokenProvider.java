package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final long expirationInMillis;
    private final long refreshExpirationInMillis;

    public JwtTokenProvider(
            @Value("${app.security.jwt.secret}") String secret,
            @Value("${app.security.jwt.expiration-in-seconds}") long expirationInSeconds,
            @Value("${app.security.jwt.refresh-expiration-in-seconds:900}") long refreshExpirationInSeconds
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationInMillis = expirationInSeconds * 1000;
        this.refreshExpirationInMillis = refreshExpirationInSeconds * 1000;
    }

    public String generateToken(Authentication authentication) {
        return generateToken(authentication, expirationInMillis);
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, refreshExpirationInMillis);
    }

    private String generateToken(Authentication authentication, long expiration) {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public long getExpirationInSeconds() {
        return expirationInMillis / 1000;
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }
}


