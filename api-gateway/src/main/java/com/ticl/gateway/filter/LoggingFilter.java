package com.ticl.gateway.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@Log4j2
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();

        String method = exchange.getRequest().getMethod().toString();
        String path = exchange.getRequest().getURI().getPath();
        String clientIp = exchange.getRequest().getRemoteAddress() != null ?
                exchange.getRequest().getRemoteAddress().getAddress().getHostAddress() : "Unknown";

        log.info("Incoming Request - Method: {}, Path: {}, Client IP: {}, Timestamp: {}",
                method, path, clientIp, LocalDateTime.now());

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long duration = System.currentTimeMillis() - startTime;
            int statusCode = exchange.getResponse().getStatusCode() != null ?
                    exchange.getResponse().getStatusCode().value() : 0;

            log.info("Outgoing Response - Method: {}, Path: {}, Status: {}, Duration: {}ms",
                    method, path, statusCode, duration);
        }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
