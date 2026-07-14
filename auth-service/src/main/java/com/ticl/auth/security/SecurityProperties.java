package com.ticl.auth.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "security")
@Getter
@Setter
@Component
public class SecurityProperties {
    private List<String> publicUrls;
}