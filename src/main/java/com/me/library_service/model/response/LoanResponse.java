package com.me.library_service.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LoanResponse extends BookResponse{

    private BookResponse book;
    private UserResponse user;
    private LocalDate checkoutDate;
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;
}
