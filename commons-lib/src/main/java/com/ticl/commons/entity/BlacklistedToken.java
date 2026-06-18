package com.ticl.commons.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "token_blacklist")
@Data
@Builder
public class BlacklistedToken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 3000)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryTime;
}
