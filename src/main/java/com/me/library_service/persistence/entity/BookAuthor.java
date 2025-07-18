package com.me.library_service.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "book_authors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BookAuthor extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "book_fk")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "author_fk")
    private Author author;

}
