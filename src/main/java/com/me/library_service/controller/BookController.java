package com.me.library_service.controller;

import com.me.library_service.model.request.BookRequest;
import com.me.library_service.model.request.BookUpdateRequest;
import com.me.library_service.model.response.ApiResponse;
import com.me.library_service.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@Slf4j
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody BookRequest bookRequest) {
        log.debug("[+] BookController:create with request dto: {}", bookRequest);
        ApiResponse apiResponse = bookService.create(bookRequest);
        log.debug("[+] BookController:create response book: {}", apiResponse);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody BookUpdateRequest bookUpdateRequest) {
        log.debug("[+] BookController:update with request dto: {}", bookUpdateRequest);
        ApiResponse apiResponse = bookService.update(bookUpdateRequest);
        log.debug("[+] BookController:update response book: {}", apiResponse);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        log.debug("[+] BookController:findById with request dto: {}", id);
        ApiResponse apiResponse = bookService.findById(id);
        log.debug("[+] BookController:findById response : {}", apiResponse);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    public ResponseEntity<?> findAll(Pageable pageable) {
        log.debug("[+] BookController:findAll with request page: {}", pageable);
        ApiResponse apiResponse = bookService.findAll(pageable);
        log.debug("[+] BookController:findAll response : {}", apiResponse);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) LocalDate createdAt,
            Pageable pageable
    ) {
        log.debug("[+] BookController:search with request page: {}", pageable);
        ApiResponse apiResponse = bookService.search(title, isbn, publisher, createdAt, pageable);
        log.debug("[+] BookController:search response : {}", apiResponse);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping
    public ResponseEntity<?> findAuthorById(@PathVariable Long id) {
        log.debug("[+] BookController:delete with book id: {}", id);
        ApiResponse apiResponse = bookService.delete(id);
        log.debug("[+] BookController:delete response : {}", apiResponse);
        return ResponseEntity.ok(apiResponse);
    }


    @GetMapping("author/by-book-id/{id}")
    public ResponseEntity<?> findAuthorByBookId(@PathVariable Long id,  Pageable pageable) {
        log.debug("[+] BookController:findAuthorByBookId with request id: {} & page: {}", id, pageable);
        ApiResponse apiResponse = bookService.findAuthorByBookId(id, pageable);
        log.debug("[+] BookController:findAuthorByBookId response : {}", apiResponse);
        return ResponseEntity.ok(apiResponse);
    }

}

