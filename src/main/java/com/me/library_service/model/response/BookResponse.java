package com.me.library_service.model.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BookResponse extends BaseResponse{

    private String title;
    private String isbn;
    private String revisionNumber;
    private LocalDate publishedDate;
    private String publisher;
    private String genre;
    private String coverImageUrl;

}
