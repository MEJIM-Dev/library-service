package com.me.library_service.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BookUpdateRequest {

    @NotNull(message = "Id cannot be null")
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "ISBN cannot be blank")
    private String isbn;

    private String revisionNumber;

    private LocalDate publishedDate;

    private String publisher;

    private String genre;

    private String coverImageUrl;
}
