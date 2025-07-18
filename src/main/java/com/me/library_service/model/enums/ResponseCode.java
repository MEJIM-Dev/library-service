package com.me.library_service.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    SUCCESSFUL("00", "Successful"),
    FAIL("99", "Failed"),
    INVALID_USER("97", "Invalid User"),
    PENDING("56", "pending"),;

    private final String status;
    private final String message;

}
