package com.me.library_service.service.impl;

import com.me.library_service.exception.CustomException;
import com.me.library_service.model.enums.ResponseCode;
import com.me.library_service.model.request.BookRequest;
import com.me.library_service.model.request.BookUpdateRequest;
import com.me.library_service.model.response.ApiResponse;
import com.me.library_service.model.response.BookResponse;
import com.me.library_service.model.response.PagedResponse;
import com.me.library_service.persistence.entity.Author;
import com.me.library_service.persistence.entity.Book;
import com.me.library_service.persistence.entity.BookAuthor;
import com.me.library_service.persistence.repository.AuthorRepository;
import com.me.library_service.persistence.repository.BookAuthorRepository;
import com.me.library_service.persistence.repository.BookRepository;
import com.me.library_service.persistence.repository.specification.BookSpecification;
import com.me.library_service.service.BookService;
import com.me.library_service.util.ModelMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookAuthorRepository bookAuthorRepository;

    @Override
    @Transactional
    public ApiResponse create(BookRequest bookRequest) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ResponseCode.FAIL.getMessage());

        try {
            List<Author> authors = bookRequest.getAuthors().stream()
                    .map(name -> authorRepository.findByNameIgnoreCase(name)
                            .orElseGet(() -> authorRepository.save(Author.builder().name(name).build())))
                    .collect(Collectors.toList());


            Book book = Book.builder()
                    .title(bookRequest.getTitle())
                    .isbn(bookRequest.getIsbn())
                    .revisionNumber(bookRequest.getRevisionNumber())
                    .publishedDate(bookRequest.getPublishedDate())
                    .publisher(bookRequest.getPublisher())
                    .genre(bookRequest.getGenre())
                    .coverImageUrl(bookRequest.getCoverImageUrl())
                    .build();

            Book saved = bookRepository.save(book);

            authors.forEach(author -> {
                BookAuthor bookAuthor = BookAuthor.builder()
                        .author(author)
                        .book(saved)
                        .build();

                bookAuthorRepository.save(bookAuthor);
            });

            apiResponse.setStatus(ResponseCode.SUCCESSFUL.getStatus());
            apiResponse.setMessage(ResponseCode.SUCCESSFUL.getMessage());
        } catch (Exception e){
            log.error("Error: {}", e.getStackTrace());
        }

        return apiResponse;
    }

    @Override
    @Transactional
    public ApiResponse update(BookUpdateRequest bookUpdateRequest) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ResponseCode.FAIL.getMessage());

        try {
            Book book = bookRepository.findById(bookUpdateRequest.getId())
                    .orElseThrow(() -> new CustomException("Book not found", HttpStatus.BAD_REQUEST));

            book.setTitle(bookUpdateRequest.getTitle());
            book.setIsbn(bookUpdateRequest.getIsbn());
            book.setRevisionNumber(bookUpdateRequest.getRevisionNumber());
            book.setPublishedDate(bookUpdateRequest.getPublishedDate());
            book.setPublisher(bookUpdateRequest.getPublisher());
            book.setGenre(bookUpdateRequest.getGenre());
            book.setCoverImageUrl(bookUpdateRequest.getCoverImageUrl());
            bookRepository.save(book);

            apiResponse.setStatus(ResponseCode.SUCCESSFUL.getStatus());
            apiResponse.setMessage(ResponseCode.SUCCESSFUL.getMessage());
        } catch (Exception e){
            log.error("Error: {}", e.getStackTrace());
        }

        return apiResponse;
    }


    @Override
    public ApiResponse findById(Long id) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ResponseCode.FAIL.getMessage());

        try {
            Book book = bookRepository.findById(id)
                    .orElseThrow(()-> new CustomException("Book not found", HttpStatus.BAD_REQUEST));

            BookResponse bookResponse = ModelMapperUtils.map(book, BookResponse.class);

            apiResponse.setStatus(ResponseCode.SUCCESSFUL.getStatus());
            apiResponse.setMessage(ResponseCode.SUCCESSFUL.getMessage());
            apiResponse.setData(bookResponse);
        } catch (Exception e){
            log.error("Error: {}", e.getStackTrace());
        }

        return apiResponse;
    }

    @Override
    public ApiResponse findAll(Pageable pageable) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ResponseCode.FAIL.getMessage());

        try {
            Page<Book> books = bookRepository.findAll(pageable);

            PagedResponse<BookResponse> pagedResponse = PagedResponse.<BookResponse>builder()
                    .content(books.map(book -> ModelMapperUtils.map(book, BookResponse.class)).getContent())
                    .totalElements(books.getTotalElements())
                    .build();

            apiResponse.setStatus(ResponseCode.SUCCESSFUL.getStatus());
            apiResponse.setMessage(ResponseCode.SUCCESSFUL.getMessage());
            apiResponse.setData(pagedResponse);
        } catch (Exception e){
            log.error("Error: {}", e.getStackTrace());
        }

        return apiResponse;
    }

    @Override
    public ApiResponse search(String title, String isbn, String publisher, LocalDate createdAt, Pageable pageable) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ResponseCode.FAIL.getMessage());

        try {
            Specification<Book> spec = BookSpecification.getCriteria(title, isbn, publisher, createdAt);

            Page<Book> books = bookRepository.findAll(spec, pageable);

            PagedResponse<BookResponse> pagedResponse = PagedResponse.<BookResponse>builder()
                    .content(books.map(book -> ModelMapperUtils.map(book, BookResponse.class)).getContent())
                    .totalElements(books.getTotalElements())
                    .totalPages(books.getTotalPages())
                    .build();

            apiResponse.setStatus(ResponseCode.SUCCESSFUL.getStatus());
            apiResponse.setMessage(ResponseCode.SUCCESSFUL.getMessage());
            apiResponse.setData(pagedResponse);
        } catch (Exception e){
            log.error("Error: {}", e.getStackTrace());
        }

        return apiResponse;
    }

    @Override
    @Transactional
    public ApiResponse delete(Long id) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ResponseCode.FAIL.getMessage());

        try {
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Book not found", HttpStatus.BAD_REQUEST));

            bookRepository.delete(book);

            List<BookAuthor> bookAuthors = bookAuthorRepository.findAllByBook(book);

            bookAuthors.forEach(bookAuthor -> {
                bookAuthorRepository.delete(bookAuthor);
            });

            apiResponse.setStatus(ResponseCode.SUCCESSFUL.getStatus());
            apiResponse.setMessage(ResponseCode.SUCCESSFUL.getMessage());
        } catch (Exception e){
            log.error("Error: {}", e.getStackTrace());
        }

        return apiResponse;
    }

    @Override
    public ApiResponse findAuthorByBookId(Long id, Pageable pageable) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ResponseCode.FAIL.getMessage());

        try {
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Book not found", HttpStatus.BAD_REQUEST));

            Page<BookAuthor> bookAuthors = bookAuthorRepository.findByBook(book, pageable);

            PagedResponse<BookResponse> pagedResponse = PagedResponse.<BookResponse>builder()
                    .content(bookAuthors.map(bookAuthor -> ModelMapperUtils.map(bookAuthor, BookResponse.class)).getContent())
                    .totalElements(bookAuthors.getTotalElements())
                    .totalPages(bookAuthors.getTotalPages())
                    .build();

            apiResponse.setStatus(ResponseCode.SUCCESSFUL.getStatus());
            apiResponse.setMessage(ResponseCode.SUCCESSFUL.getMessage());
            apiResponse.setData(pagedResponse);
        } catch (Exception e){
            log.error("Error: {}", e.getStackTrace());
        }

        return apiResponse;
    }
}
