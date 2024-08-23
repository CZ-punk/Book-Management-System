package com.msa.review.core;

import com.msa.review.auditor.BaseEntity;
import com.msa.review.reviews.dto.ReviewDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Review extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private Long bookId;        // book table ref
    private String username;    // user table ref

    private String content;

    @Range(min = 1, max = 5)
    private Integer rating;

    public Review(ReviewDto dto) {
        bookId = dto.getBookId();
        username = dto.getUsername();
        content = dto.getContent();
        rating = dto.getRating();
    }

    public Review updateInfo(ReviewDto reviewDto) {
        content = reviewDto.getContent();
        rating = reviewDto.getRating();
        return this;
    }
}
