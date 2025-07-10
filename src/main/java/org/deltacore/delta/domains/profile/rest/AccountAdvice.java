package org.deltacore.delta.domains.profile.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.deltacore.delta.domains.profile.exception.ConflictException;
import org.deltacore.delta.domains.profile.exception.IllegalArgumentException;
import org.deltacore.delta.domains.profile.exception.ProfileNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class AccountAdvice {
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, Object>> handleConflictException(ConflictException ex, HttpServletRequest request) {
        return getMapResponseEntity(request, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleConflictException(IllegalArgumentException ex, HttpServletRequest request) {
        return getMapResponseEntity(request, ex.getMessage());
    }

    @ExceptionHandler(ProfileNotFound.class)
    public ResponseEntity<Map<String, Object>> handleConflictException(ProfileNotFound ex, HttpServletRequest request) {
        return getMapResponseEntity(request, ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> getMapResponseEntity(HttpServletRequest request, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Conflict");
        body.put("message", message);
        body.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

}
