package com.welderhayne.Oauth.Exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandlingController {
    @ExceptionHandler(value = DuplicateKeyException.class)
    public ResponseEntity<ExceptionsMessager> exceptionDuplicateKeyException(DuplicateKeyException error) {
        ExceptionsMessager messager = new ExceptionsMessager(
                HttpStatus.CONFLICT,
                error.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(messager.getStatus()).body(messager);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionsMessager> exceptionDataIntegrityViolationException(DataIntegrityViolationException error) {
        ExceptionsMessager messager = new ExceptionsMessager(
                HttpStatus.CONFLICT,
                error.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(messager.getStatus()).body(messager);
    }

    @ExceptionHandler(value = XssCharTokenInvalidException.class)
    public ResponseEntity<ExceptionsMessager> exceptionXssCharTokenInvalid(XssCharTokenInvalidException error) {
        ExceptionsMessager messager = new ExceptionsMessager(
                HttpStatus.BAD_REQUEST,
                error.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(messager.getStatus()).body(messager);
    }

    @ExceptionHandler(value = UnsupportedOperationException.class)
    public ResponseEntity<ExceptionsMessager> exceptionUnsupportedOperation(UnsupportedOperationException error) {
        ExceptionsMessager messager = new ExceptionsMessager(
                HttpStatus.BAD_REQUEST,
                error.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(messager.getStatus()).body(messager);
    }
}
