package org.deltacore.delta.domains.profile.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.deltacore.delta.domains.profile.exception.*;
import org.deltacore.delta.domains.profile.exception.IllegalArgumentException;
import org.deltacore.delta.shared.exception.AdviceStruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class AccountAdvice extends AdviceStruct {
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, Object>> handleConflictException(ConflictException ex, HttpServletRequest request) {
        return getMapResponseEntity(request, ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleConflictException(IllegalArgumentException ex, HttpServletRequest request) {
        return getMapResponseEntity(request, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleConflictException(ProfileNotFoundException ex, HttpServletRequest request) {
        return getMapResponseEntity(request, ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<Map<String, Object>> handleEmptyFileException(EmptyFileException ex, HttpServletRequest request) {
        return getMapResponseEntity(request, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProfileImageTooLargeException.class)
    public ResponseEntity<Map<String, Object>> handleProfileImageTooLargeException(ProfileImageTooLargeException ex, HttpServletRequest request) {
        return getMapResponseEntity(request, ex.getMessage(), HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidFileTypeException(InvalidFileTypeException ex, HttpServletRequest request) {
        return getMapResponseEntity(request, ex.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(ProfileImageNotSetException.class)
    public ResponseEntity<Map<String, Object>> handleProfileImageNotSetException(ProfileImageNotSetException ex, HttpServletRequest request) {
        return getMapResponseEntity(request, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
