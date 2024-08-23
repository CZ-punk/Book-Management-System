package com.msa.review.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    private String title;
    private String author;
    private String description;
    private String category;
    private LocalDateTime publishedAt;

}
