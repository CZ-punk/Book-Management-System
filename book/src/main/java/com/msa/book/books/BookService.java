package com.msa.book.books;

import com.msa.book.books.dto.BookDto;
import com.msa.book.books.dto.BookInfoDto;
import com.msa.book.client.ReviewClient;
import com.msa.book.client.ReviewDto;
import com.msa.book.core.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final ReviewClient reviewClient;

    @Transactional
    public BookDto createBook(BookDto bookDto) {
        Book book = new Book(bookDto);
        bookRepository.save(book);
        return bookDto;
    }

    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(BookDto::new)
                .collect(Collectors.toList());
    }

    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND BOOK ID: " + id));

        ResponseEntity<List<ReviewDto>> reviews = reviewClient.findAllByBookId(id);
        if (reviews.getStatusCode() != HttpStatus.OK) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND REVIEW BOOK ID : " + id);
        List<ReviewDto> reviewDtoList = reviews.getBody();
        return new BookInfoDto(book, reviewDtoList);
    }

    @Transactional
    public BookDto updateBook(Long id, BookDto bookDto) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND BOOK ID: " + id));
        return new BookDto(book.updateInfo(bookDto));
    }

    @Transactional
    public BookDto deleteBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND BOOK ID: " + id));
        ResponseEntity<List<ReviewDto>> reviews = reviewClient.findAllByBookId(id);
        if (reviews.getStatusCode() != HttpStatus.OK) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND REVIEW BOOK ID : " + id);
        if (!reviews.getBody().isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "NOT ACCEPTABLE REVIEW EXISTS");
        bookRepository.delete(book);
        return new BookDto(book);
    }
}
