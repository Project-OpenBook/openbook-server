package com.openbook.openbook.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class OpenBookException extends RuntimeException{
    private final HttpStatus httpStatus;
    private final String message;

    public OpenBookException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
