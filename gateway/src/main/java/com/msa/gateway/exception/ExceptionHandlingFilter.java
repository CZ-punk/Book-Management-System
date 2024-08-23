package com.msa.gateway.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class ExceptionHandlingFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        return chain.filter(exchange)
                .onErrorResume(throwable -> {
                    if (throwable instanceof JwtAuthenticationException) {
                        log.info("Handler! ", throwable);

                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

                        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
                        DataBuffer dataBuffer = bufferFactory.wrap(throwable.getMessage().getBytes(StandardCharsets.UTF_8));
                        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
                    }
                    
                    // 다른 예외 추가
                    return Mono.error(throwable);
                });
    }
}
