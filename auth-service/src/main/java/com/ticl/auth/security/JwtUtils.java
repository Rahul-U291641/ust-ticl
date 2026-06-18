package com.ticl.auth.security;

import com.ticl.auth.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(User user) {

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("role", user.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(
                        Keys.hmacShaKeyFor(SECRET.getBytes()),
                        Jwts.SIG.HS256
                )
                .compact();
    }

    public String extractUsername(String token) {

        return Jwts.parser()
                .verifyWith(
                        Keys.hmacShaKeyFor(SECRET.getBytes())
                )
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Date extractExpiration(String token) {

        return Jwts.parser()
                .verifyWith(
                        Keys.hmacShaKeyFor(SECRET.getBytes())
                )
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }
}
