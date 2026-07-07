package com.ticl.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    private List<String> publicUrls;

    public List<String> getPublicUrls() {
        return publicUrls;
    }

    public void setPublicUrls(List<String> publicUrls) {
        this.publicUrls = publicUrls;
    }
}
