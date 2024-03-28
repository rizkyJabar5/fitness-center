package com.jabar.app.fitnesscenter.app.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AppRuntimeException extends RuntimeException {

    public AppRuntimeException(String message) {
        super(message);
    }

    public AppRuntimeException(Exception e) {
        super(e);
    }

    public AppRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
