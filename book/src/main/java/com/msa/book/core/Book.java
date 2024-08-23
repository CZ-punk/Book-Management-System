package com.msa.book.core;

import com.msa.book.auditor.BaseEntity;
import com.msa.book.books.dto.BookDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Book extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String title;
    private String author;
    private String description;
    private String category;
    private LocalDateTime publishedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;


    public Book(BookDto bookRequestDto) {

        title = bookRequestDto.getTitle();
        author = bookRequestDto.getAuthor();
        description = bookRequestDto.getDescription();
        category = bookRequestDto.getCategory();
        publishedAt = bookRequestDto.getPublishedAt();
    }

    public Book updateInfo(BookDto bookDto) {
        title = bookDto.getTitle() != null ? bookDto.getTitle() : title;
        author = bookDto.getAuthor() != null ? bookDto.getAuthor() : author;
        description = bookDto.getDescription() != null ? bookDto.getDescription() : description;
        category = bookDto.getCategory() != null ? bookDto.getCategory() : category;
        publishedAt = bookDto.getPublishedAt() != null ? bookDto.getPublishedAt() : publishedAt;
        return this;
    }
}
