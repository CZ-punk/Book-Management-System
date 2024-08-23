package com.msa.book.books;

import com.msa.book.books.dto.BookDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookRequestDto, HttpServletRequest request) {
        if (!ROLE_ADMIN_CHECK(request)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "YOUR ARE NOT ADMIN");
        return ResponseEntity.ok(bookService.createBook(bookRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> findAll() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @RequestBody BookDto bookDto, HttpServletRequest request) {
        if (!ROLE_ADMIN_CHECK(request)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "YOUR ARE NOT ADMIN");
        return ResponseEntity.ok(bookService.updateBook(id, bookDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookDto> deleteBook(@PathVariable Long id, HttpServletRequest request) {
        if (!ROLE_ADMIN_CHECK(request)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "YOUR ARE NOT ADMIN");
        return ResponseEntity.ok(bookService.deleteBook(id));
    }


    private boolean ROLE_ADMIN_CHECK(HttpServletRequest request) {
        String role = request.getHeader("X-ROLE");
        log.info("ADMIN CHECK {}", role);
        if (role == null || role.equals("ROLE_USER")) return false;
        return role.equals("ROLE_ADMIN");
    }

}
