package com.me.library_service.persistence.repository;

import com.me.library_service.persistence.entity.Book;
import com.me.library_service.persistence.entity.BookAuthor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, Long> {
    Page<BookAuthor> findByBook(Book book, Pageable pageable);

    List<BookAuthor> findAllByBook(Book book);
}
