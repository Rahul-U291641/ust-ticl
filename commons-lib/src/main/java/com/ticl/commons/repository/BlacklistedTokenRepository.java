package com.ticl.commons.repository;

import com.ticl.commons.entity.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, UUID> {
    boolean existsByToken(String token);
    @Modifying
    @Transactional
    @Query("""
       DELETE FROM BlacklistedToken b
       WHERE b.expiryTime < :now
       """)
    void deleteExpiredTokens(
            @Param("now")
            LocalDateTime now);
}
