package com.msa.review.reviews;

import com.msa.review.client.BookDto;
import com.msa.review.reviews.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewDto createReviewDto, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(reviewService.createReview(createReviewDto, token));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReviewDto>> findAllByBookId(@PathVariable Long bookId) {
        return ResponseEntity.ok(reviewService.findAllByBookId(bookId));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<ReviewDto>> findAllByUsername(@PathVariable String username, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(reviewService.findAllByUsername(username, token));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> updateById(@PathVariable Long id, @RequestBody ReviewDto reviewDto, @RequestHeader("Authorization") String token, @RequestHeader("X-USERNAME") String username) {
        return ResponseEntity.ok(reviewService.updateById(id, reviewDto, token, username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReviewDto> updateById(@PathVariable Long id, @RequestHeader("Authorization") String token, @RequestHeader("X-USERNAME") String username) {
        return ResponseEntity.ok(reviewService.deleteById(id, token, username));
    }


}
