package com.ticl.auth.service;

import com.ticl.auth.dto.ApiResponse;
import com.ticl.auth.dto.LoginRequest;
import com.ticl.auth.dto.LoginResponse;
import com.ticl.auth.dto.UserInfoResponse;
import com.ticl.auth.entity.BlacklistedToken;
import com.ticl.auth.entity.User;
import com.ticl.auth.exception.customExceptions.UsernameNotFoundException;
import com.ticl.auth.repository.BlacklistedTokenRepository;
import com.ticl.auth.repository.UserRepository;
import com.ticl.auth.security.JwtUtils;
import org.jspecify.annotations.Nullable;
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

        Date expiryDate = jwtUtils.extractExpiration(token);
        BlacklistedToken blacklistedToken = BlacklistedToken.builder()
                .token(token)
                .expiryTime(
                        expiryDate.toInstant()
                                .atZone(
                                        ZoneId.systemDefault())
                                .toLocalDateTime())
                .createdAt(LocalDateTime.now())
                .build();

        blacklistedTokenRepository.save(blacklistedToken);
    }
}