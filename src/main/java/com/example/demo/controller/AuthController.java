package com.example.demo.controller;

import com.example.demo.security.JwtUtil;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    static class LoginRequest { public String username; public String password; }

    static class RefreshRequest { public String refreshToken; }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        return userRepository.findByUsername(req.username)
                .filter(u -> passwordEncoder.matches(req.password, u.getPassword()))
                .map(u -> Map.of(
                        "accessToken", jwtUtil.generateAccessToken(u.getUsername()),
                        "refreshToken", jwtUtil.generateRefreshToken(u.getUsername())
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).body(Map.of("error", "Invalid credentials")));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest req) {
        if (req == null || req.refreshToken == null) return ResponseEntity.badRequest().build();
        String subject = jwtUtil.validateRefreshTokenAndGetSubject(req.refreshToken);
        if (subject == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(Map.of("accessToken", jwtUtil.generateAccessToken(subject)));
    }
}
