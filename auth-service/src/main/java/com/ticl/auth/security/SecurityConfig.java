package com.ticl.auth.security;

import com.ticl.auth.entity.Role;
import com.ticl.auth.entity.User;
import com.ticl.auth.exception.customExceptions.CustomAccessDeniedHandler;
import com.ticl.auth.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Log4j2
public class SecurityConfig {
    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsServiceImpl userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationProvider authenticationProvider)
            throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .authenticationProvider(authenticationProvider)
                .exceptionHandling(
                        exception -> exception.accessDeniedHandler(accessDeniedHandler)
                )
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Create users at runtime
     * */
    @Bean
    CommandLineRunner loadUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (userRepository.count() > 0) {
                log.warn("Default users are already present!");
                return;
            }

            LocalDateTime now = LocalDateTime.now();

            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("Admin@123"))
                    .role(Role.ADMIN)
                    .status("ACTIVE")
                    .created_at(now)
                    .updated_at(now)
                    .build();

            User manager = User.builder()
                    .username("inventory.manager")
                    .password(passwordEncoder.encode("Manager@123"))
                    .role(Role.INVENTORY_MANAGER)
                    .status("ACTIVE")
                    .created_at(now)
                    .updated_at(now)
                    .build();

            User viewer = User.builder()
                    .username("viewer")
                    .password(passwordEncoder.encode("Viewer@123"))
                    .role(Role.VIEWER)
                    .status("ACTIVE")
                    .created_at(now)
                    .updated_at(now)
                    .build();

            userRepository.save(admin);
            userRepository.save(manager);
            userRepository.save(viewer);

           log.info("All Default users created successfully.");
        };
    }
}
