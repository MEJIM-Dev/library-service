package com.me.library_service.persistence.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Book extends BaseEntity{

    @Column(name = "title")
    private String title;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "revision_number")
    private String revisionNumber;

    @Column(name = "published_date")
    private LocalDate publishedDate;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "genre")
    private String genre;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

}
