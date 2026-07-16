package com.ticl.auth.service;

import com.ticl.auth.dto.ValidateTokenResponse;
import com.ticl.auth.dto.LoginRequest;
import com.ticl.auth.dto.LoginResponse;
import com.ticl.auth.dto.UserInfoResponse;
import com.ticl.auth.entity.BlacklistedToken;
import com.ticl.auth.entity.User;
import com.ticl.auth.producer.AuthEventProducer;
import com.ticl.auth.repository.BlacklistedTokenRepository;
import com.ticl.auth.repository.UserRepository;
import com.ticl.auth.utils.JwtUtils;
import com.ticl.commons.dto.ApiResponse;
import com.ticl.commons.enums.EventType;
import com.ticl.commons.exception.customExceptions.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BlacklistedTokenRepository blacklistedTokenRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthEventProducer authEventProducer;

    public ValidateTokenResponse validateToken(String token) {
        if (blacklistedTokenRepository.existsByToken(token)) {
            throw new BadCredentialsException("Token is blacklisted");
        }
        try {
            if (!jwtUtils.isTokenValid(token)) {
                throw new BadCredentialsException("Token is invalid or expired");
            }
        } catch (Exception e) {
            throw new BadCredentialsException("Token is invalid or expired");
        }
        String username = jwtUtils.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ValidateTokenResponse.builder()
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }

    public LoginResponse login(LoginRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            String token = jwtUtils.generateToken(user);
            authEventProducer.publishUserAuthEvent(user, EventType.USER_LOGGED_IN);
            return LoginResponse.builder()
                    .token(token)
                    .username(user.getUsername())
                    .role(user.getRole().name())
                    .expiresIn(3600)
                    .build();
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public ApiResponse<UserInfoResponse> getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new ApiResponse<>(true,
                "Valid user details",
                UserInfoResponse.builder()
                        .username(user.getUsername())
                        .role(user.getRole().name())
                        .build());
    }

    public void logout(String token) {
        String username = jwtUtils.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Date expiryDate = jwtUtils.extractExpiration(token);
        BlacklistedToken blacklistedToken = BlacklistedToken.builder()
                .token(token)
                .expiryTime(
                        expiryDate.toInstant()
                                .atZone(
                                        ZoneId.systemDefault())
                                .toLocalDateTime())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        blacklistedTokenRepository.save(blacklistedToken);
        authEventProducer.publishUserAuthEvent(user, EventType.USER_LOGGED_OUT);
    }
}