package com.msa.book.books.dto;

import com.msa.book.core.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    protected String title;
    protected String author;
    protected String description;
    protected String category;
    protected LocalDateTime publishedAt;

    public BookDto(Book book) {
        title = book.getTitle();
        author = book.getAuthor();
        description = book.getDescription();
        category = book.getCategory();
        publishedAt = book.getPublishedAt();
    }
}
