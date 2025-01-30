package com.timeissecuritytime.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtTokenUtil {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 86400000; // One-day

    public String generateToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(calculateExpirationDate())
            .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
            .compact();
    }

    public boolean validateToken(String token) {
        return parseClaims(token)
                .map(this::isTokenValid)
                .orElse(false);
    }

    public String extractUsername(String token) {
        return parseClaims(token)
                .map(Claims::getSubject)
                .orElse(null);
    }

    private Date calculateExpirationDate() {
        return new Date(System.currentTimeMillis() + EXPIRATION_TIME);
    }

    private Optional<Claims> parseClaims(String token) {
        try {
            return Optional.of(Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody());
        } catch (JwtException e) {
            return Optional.empty();
        }
    }

    private boolean isTokenValid(Claims claims) {
        Date expirationDate = claims.getExpiration();
        return expirationDate != null && !expirationDate.before(new Date());
    }
}