package com.msa.book.books.dto;

import com.msa.book.client.ReviewDto;
import com.msa.book.core.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookInfoDto extends BookDto {

    private List<ReviewDto> reviewDtoList;

    public BookInfoDto(Book book, List<ReviewDto> reviewDtoList) {

        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.description = book.getDescription();
        this.category = book.getCategory();
        this.publishedAt = book.getPublishedAt();
        this.reviewDtoList = reviewDtoList;
    }


}
