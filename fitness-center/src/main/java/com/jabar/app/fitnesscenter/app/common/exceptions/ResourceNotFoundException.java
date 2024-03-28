package com.jabar.app.fitnesscenter.app.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends AppRuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
