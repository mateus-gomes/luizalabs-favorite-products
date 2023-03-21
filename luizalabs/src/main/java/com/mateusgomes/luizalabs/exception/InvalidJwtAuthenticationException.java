package com.mateusgomes.luizalabs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidJwtAuthenticationException extends AuthenticationException {

    private String exception;

    public InvalidJwtAuthenticationException(String message) {
        super(message);
        this.exception = message;
    }
}
