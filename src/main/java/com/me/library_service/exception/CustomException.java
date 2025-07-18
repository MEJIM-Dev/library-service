package com.me.library_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

    private HttpStatus statusCode;

    public CustomException(String msg) {
        super(msg);
    }

    public CustomException(String msg, HttpStatus statusCode) {
        super(msg);
        this.statusCode = statusCode;
    }

}
