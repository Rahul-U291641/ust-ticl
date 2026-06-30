package com.ticl.commons.security;

import com.ticl.commons.exception.customExceptions.BusinessException;
import com.ticl.commons.exception.customExceptions.CustomAccessDeniedException;
import com.ticl.commons.repository.BlacklistedTokenRepository;
import com.ticl.commons.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@Log4j2
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    BlacklistedTokenRepository blacklistedTokenRepository;

    @Autowired
    @Qualifier("handlerExceptionResolver")  // picks the composite resolver that knows about @ControllerAdvice
    HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        log.info("JWT Filter execution started!");

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.error("Skipped JWT verification as token is empty/not found!");
            filterChain.doFilter(request, response);
            throw new BusinessException("Authorization header is missing or invalid!");
        }

        String token = authHeader.substring(7);
        log.info("Extracted JWT token from Header!");

        // Checking token is blacklisted or not before validating it
        if (blacklistedTokenRepository.existsByToken(token)) {
            log.error("JWT token is blacklisted!");
            handlerExceptionResolver.resolveException(
                    request, response, null,
                    new BusinessException("Token is blacklisted!")
            );
            return; // stop filter chain
        } else {
            try {
                if (!jwtUtils.isTokenValid(token)) {
                    log.error("JWT token is invalid!");
                    this.handleAccessDeniedException(request, response, new AccessDeniedException("Token is not valid!"));
                    return; // stop filter chain
                }
            } catch (Exception e) {
                this.handleAccessDeniedException(request, response, new AccessDeniedException(e.getMessage()));
                return;
            }
        }

        String username = jwtUtils.extractUsername(token);
        log.info("Extracted username from the token!");

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.warn("Set security context holder!");

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            log.info("Authorities: {}", userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        log.info("JWT Filter Executed");
        filterChain.doFilter(request, response);
    }

    private void handleAccessDeniedException(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) {
        log.error("Access Denied: {}", accessDeniedException.getMessage());
        handlerExceptionResolver.resolveException(
                request, response, null,
                new CustomAccessDeniedException("Token is invalid/blacklisted!", accessDeniedException)
        );
    }
}
