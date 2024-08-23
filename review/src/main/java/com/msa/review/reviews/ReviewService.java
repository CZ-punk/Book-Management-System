package com.msa.review.reviews;

import com.msa.review.client.AuthClient;
import com.msa.review.client.BookClient;
import com.msa.review.client.BookDto;
import com.msa.review.client.UserInfoDto;
import com.msa.review.core.Review;
import com.msa.review.reviews.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookClient bookClient;
    private final AuthClient authClient;

    @Transactional
    public ReviewDto createReview(ReviewDto dto, String token) {
        ResponseEntity<BookDto> book = bookClient.findById(dto.getBookId());
        if (book.getStatusCode() != HttpStatus.OK) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND BOOK ID: " + dto.getBookId());
        ResponseEntity<?> userInfo = authClient.findByUsername(dto.getUsername(), token);
        if (userInfo.getStatusCode() != HttpStatus.OK) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND username: " + dto.getUsername());

        log.info("book status code : {}", book.getStatusCode());
        log.info("book body : {}", book.getBody());
        log.info("book headers : {}", book.getHeaders());

        reviewRepository.save(new Review(dto));
        return dto;
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> findAllByBookId(Long bookId) {

        List<Review> reviews = reviewRepository.findAllByBookId(bookId);
        if (reviews.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND REVIEW BY BOOK ID: " + bookId);

        return reviews.stream()
                .map(ReviewDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> findAllByUsername(String username, String token) {
        ResponseEntity<UserInfoDto> user = authClient.findByUsername(username, token);
        if (user.getStatusCode() != HttpStatus.OK) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND USERNAME: " + username);

        return reviewRepository.findAllByUsername(username).stream()
                .map(ReviewDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewDto updateById(Long id, ReviewDto reviewDto, String token, String username) {

        Review review = reviewRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND REVIEW: " + id));
        if (!review.getBookId().equals(reviewDto.getBookId())) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND BOOK ID: " + reviewDto.getBookId());

        ResponseEntity<UserInfoDto> userInfo = authClient.findByUsername(username, token);
        if (userInfo.getStatusCode() != HttpStatus.OK) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND USERNAME: " + username);
        if (!review.getUsername().equals(userInfo.getBody().getUsername())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED USERNAME TOKEN");

        reviewRepository.save(review.updateInfo(reviewDto));
        return reviewDto;
    }

    @Transactional
    public ReviewDto deleteById(Long id, String token, String username) {
        ResponseEntity<UserInfoDto> userInfo = authClient.findByUsername(username, token);
        if (userInfo.getStatusCode() != HttpStatus.OK) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND USERNAME: " + username);

        Review review = reviewRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND REVIEW: " + id));
        if (!review.getUsername().equals(userInfo.getBody().getUsername())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED USERNAME TOKEN");
        reviewRepository.delete(review);

        return new ReviewDto(review);
    }
}
