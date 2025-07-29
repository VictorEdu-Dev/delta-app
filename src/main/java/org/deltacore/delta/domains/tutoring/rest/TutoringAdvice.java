package org.deltacore.delta.domains.tutoring.rest;

import org.deltacore.delta.domains.tutoring.exception.TutoringNotFound;
import org.deltacore.delta.shared.exception.ConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class TutoringAdvice {
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleBusiness(ConflictException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("Error:", ex.getMessage()));
    }

    @ExceptionHandler(TutoringNotFound.class)
    public ResponseEntity<?> handleBusiness(TutoringNotFound ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("Error:", ex.getMessage()));
    }
}
