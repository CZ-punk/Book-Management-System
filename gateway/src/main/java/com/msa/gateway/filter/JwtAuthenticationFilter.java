package com.msa.gateway.filter;

import com.msa.gateway.exception.JwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Slf4j(topic = "jwt-authentication-filter")
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Value("${jwt.secretKey}")
    private String secretKey;
    private final String AUTHORITY_HEADER = "Authorization";
    private final String[] PASS_PATH = {"/users/login", "/users/register"};

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        if (exchange.getRequest().getURI().getPath().startsWith(PASS_PATH[0]) ||
            exchange.getRequest().getURI().getPath().startsWith(PASS_PATH[1])) {
            return chain.filter(exchange);
        }

        Optional<String> token = Optional.ofNullable(exchange.getRequest().getHeaders().get(AUTHORITY_HEADER).getFirst());
        if (token.isEmpty() || !validationToken(token.get())) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
            DataBuffer wrap = bufferFactory.wrap("NOT VALIDATE TOKEN AND EMPTY TOKEN".getBytes(StandardCharsets.UTF_8));
            return exchange.getResponse().writeWith(Mono.just(wrap));
        }

        ServerWebExchange customExchange = setRequestInfo(exchange, extractClaims(token.get()));
        return chain.filter(customExchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

    private boolean validationToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    private Claims extractClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private ServerWebExchange setRequestInfo(ServerWebExchange exchange, Claims claims) {

        log.info("claims = {}", claims);
        log.info("claims = {}", claims.get("auth"));
        log.info("claims = {}", claims.get("username"));
        return exchange.mutate().request(
                exchange.getRequest().mutate().headers(headers -> {
                    headers.set("X-ROLE", String.valueOf(claims.get("auth")));
                    headers.set("X-USERNAME", String.valueOf(claims.get("username").toString()));
                }).build()
        ).build();

    }
}
