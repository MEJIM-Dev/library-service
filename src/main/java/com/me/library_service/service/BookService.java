package com.me.library_service.service;

import com.me.library_service.model.request.BookRequest;
import com.me.library_service.model.request.BookUpdateRequest;
import com.me.library_service.model.response.ApiResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;


public interface BookService {

    ApiResponse create(BookRequest bookRequest);

    ApiResponse update(BookUpdateRequest bookUpdateRequest);

    ApiResponse findById(Long id);

    ApiResponse findAll(Pageable pageable);

    ApiResponse search(String title, String isbn, String publisher, LocalDate createdAt, Pageable pageable);

    ApiResponse delete(Long id);

    ApiResponse findAuthorByBookId(Long id, Pageable pageable);

}
