package com.example.demo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final Algorithm accessAlgorithm;
    private final JWTVerifier accessVerifier;
    private final Algorithm refreshAlgorithm;
    private final JWTVerifier refreshVerifier;
    private final long accessExpirationMs;
    private final long refreshExpirationMs;

    public JwtUtil(@Value("${jwt.secret}") String accessSecret,
                   @Value("${jwt.refresh-secret}") String refreshSecret,
                   @Value("${jwt.access-expiration-ms}") long accessExpirationMs,
                   @Value("${jwt.refresh-expiration-ms}") long refreshExpirationMs) {
        this.accessAlgorithm = Algorithm.HMAC256(accessSecret);
        this.accessVerifier = JWT.require(accessAlgorithm).build();
        this.refreshAlgorithm = Algorithm.HMAC256(refreshSecret);
        this.refreshVerifier = JWT.require(refreshAlgorithm).build();
        this.accessExpirationMs = accessExpirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    public String generateAccessToken(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + accessExpirationMs);
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(now)
                .withExpiresAt(exp)
                .sign(accessAlgorithm);
    }

    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + refreshExpirationMs);
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(now)
                .withExpiresAt(exp)
                .sign(refreshAlgorithm);
    }

    public String validateAccessTokenAndGetSubject(String token) {
        try {
            DecodedJWT jwt = accessVerifier.verify(token);
            return jwt.getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public String validateRefreshTokenAndGetSubject(String token) {
        try {
            DecodedJWT jwt = refreshVerifier.verify(token);
            return jwt.getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }
}
