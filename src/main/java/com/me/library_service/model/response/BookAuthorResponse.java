package com.me.library_service.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BookAuthorResponse extends BookResponse{

    private BookResponse book;
    private AuthResponse author;

}
