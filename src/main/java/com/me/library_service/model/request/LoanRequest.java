package com.me.library_service.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoanRequest {

    @NotNull(message = "Book ID cannot be null")
    private Long bookId;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

}
