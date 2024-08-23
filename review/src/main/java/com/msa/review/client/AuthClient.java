package com.msa.review.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service")
public interface AuthClient {

    @GetMapping("/users/{username}")
    ResponseEntity<UserInfoDto> findByUsername(@PathVariable String username, @RequestHeader("Authorization") String token);
}
