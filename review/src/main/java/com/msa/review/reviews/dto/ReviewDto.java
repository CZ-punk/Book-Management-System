package com.msa.review.reviews.dto;

import com.msa.review.core.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {

    private Long bookId;
    private String username;
    private String content;

    @Range(min = 1, max = 5)
    private Integer rating;

    public ReviewDto(Review review) {

        bookId = review.getBookId();
        username = review.getUsername();
        content = review.getContent();
        rating = review.getRating();
    }
}
