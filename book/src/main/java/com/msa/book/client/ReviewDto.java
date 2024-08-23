package com.msa.book.client;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ReviewDto {

    private Long bookId;
    private String username;
    private String content;
    private Integer rating;

}
