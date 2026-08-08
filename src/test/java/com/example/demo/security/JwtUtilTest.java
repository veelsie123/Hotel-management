package com.example.demo.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil("access-secret", "refresh-secret", 10_000L, 20_000L);
    }

    @Test
    void generateAndValidateAccessToken_returnsSubject() {
        String token = jwtUtil.generateAccessToken("admin-user");

        String subject = jwtUtil.validateAccessTokenAndGetSubject(token);

        assertEquals("admin-user", subject);
    }

    @Test
    void generateAndValidateRefreshToken_returnsSubject() {
        String token = jwtUtil.generateRefreshToken("admin-user");

        String subject = jwtUtil.validateRefreshTokenAndGetSubject(token);

        assertEquals("admin-user", subject);
    }

    @Test
    void validateAccessTokenAndGetSubject_returnsNullForInvalidToken() {
        String invalidToken = "bad.token.value";

        String subject = jwtUtil.validateAccessTokenAndGetSubject(invalidToken);

        assertNull(subject);
    }

    @Test
    void validateRefreshTokenAndGetSubject_returnsNullForInvalidToken() {
        String invalidToken = "bad.token.value";

        String subject = jwtUtil.validateRefreshTokenAndGetSubject(invalidToken);

        assertNull(subject);
    }
}
