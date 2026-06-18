package com.ticl.auth.repository;

import com.ticl.auth.entity.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, UUID> {
    boolean existsByToken(String token);
}
