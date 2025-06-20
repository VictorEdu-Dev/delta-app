package org.deltacore.delta.domains.auth.rest;

import org.deltacore.delta.domains.auth.exception.InvalidTokenException;
import org.deltacore.delta.domains.auth.exception.UserNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class AuthExceptionAdvice {
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> handleBusiness(InvalidTokenException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("Error:", ex.getMessage()));
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFound ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("Error:", ex.getMessage()));
    }
}
