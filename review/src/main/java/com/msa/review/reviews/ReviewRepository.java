package com.msa.review.reviews;

import com.msa.review.core.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByBookId(Long bookId);
    List<Review> findAllByUsername(String username);

}
