package com.mateusgomes.luizalabs.handler;

import com.mateusgomes.luizalabs.exception.ExceptionResponse;
import com.mateusgomes.luizalabs.exception.InvalidGivenIdException;
import com.mateusgomes.luizalabs.exception.InvalidJwtAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericExceptions(Exception exception, WebRequest request){
        ExceptionResponse exceptionResponse = buildExceptionResponse(exception, request);

        return new ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidGivenIdException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidGivenIdExceptions(Exception exception, WebRequest request){
        ExceptionResponse exceptionResponse = buildExceptionResponse(exception, request);

        return new ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidJwtAuthenticationExceptions(
            Exception exception, WebRequest request
    ){
        ExceptionResponse exceptionResponse = buildExceptionResponse(exception, request);

        return new ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    private ExceptionResponse buildExceptionResponse(Exception exception, WebRequest request){
        return new ExceptionResponse(
                LocalDateTime.now(),
                exception.getMessage(),
                request.getDescription(false)
        );
    }
}
