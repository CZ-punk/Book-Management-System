package com.msa.book.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "review-service")
public interface ReviewClient {

    @GetMapping("/review/book/{bookId}")
    ResponseEntity<List<ReviewDto>> findAllByBookId(@PathVariable Long bookId);

}
