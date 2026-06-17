package com.ticl.auth.service;

import com.ticl.auth.dto.LoginRequest;
import com.ticl.auth.dto.LoginResponse;
import com.ticl.auth.entity.User;
import com.ticl.auth.exception.customExceptions.UsernameNotFoundException;
import com.ticl.auth.repository.UserRepository;
import com.ticl.auth.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

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
}