package com.ticl.gateway.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.webflux.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
@Order(-2)
@Log4j2
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    @Value("${gateway.exception-handler.order:-2}")
    private int order;

    @Value("${gateway.exception-handler.fallback-error-code:GATEWAY_ERROR}")
    private String fallbackErrorCode;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status;
        String error;
        String message;

        if (ex instanceof ResponseStatusException rse) {
            status = HttpStatus.valueOf(rse.getStatusCode().value());
            error = status.getReasonPhrase();
            message = rse.getReason() != null ? rse.getReason() : ex.getMessage();
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            error = fallbackErrorCode;
            message = ex.getMessage();
            log.error("Gateway error: {}", ex.getMessage(), ex);
        }

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = "{\"status\":" + status.value() + ",\"error\":\"" + error + "\",\"message\":\"" + message + "\",\"timestamp\":\"" + LocalDateTime.now() + "\"}";
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
