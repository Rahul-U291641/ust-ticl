package com.ticl.auth.controller;

import com.ticl.auth.dto.LoginRequest;
import com.ticl.auth.dto.LoginResponse;
import com.ticl.auth.service.AuthService;
import com.ticl.commons.dto.ApiResponse;
import com.ticl.auth.dto.ValidateTokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getLoggedInUser(){
        return ResponseEntity.ok(authService.getLoggedInUser());
    }

    @GetMapping("/validate")
    public ResponseEntity<ValidateTokenResponse> validate(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return ResponseEntity.ok(authService.validateToken(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            authService.logout(token);
        }

        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }
}
