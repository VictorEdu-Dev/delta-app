package org.deltacore.delta.domains.auth.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.deltacore.delta.domains.auth.exception.InvalidRecoveryCodeException;
import org.deltacore.delta.domains.auth.exception.InvalidTokenException;
import org.deltacore.delta.domains.auth.exception.UserAlreadyExists;
import org.deltacore.delta.domains.profile.exception.ProfileImageNotSetException;
import org.deltacore.delta.domains.profile.exception.UserNotFoundException;
import org.deltacore.delta.shared.exception.AdviceStruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class AuthExceptionAdvice extends AdviceStruct {
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> handleBusiness(InvalidTokenException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("Error:", ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("Error:", ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(UserAlreadyExists ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("Error:", "User already exists: " + ex.getMessage()));
    }

    @ExceptionHandler(InvalidRecoveryCodeException.class)
    public ResponseEntity<Map<String, Object>> handleProfileImageNotSetException(InvalidRecoveryCodeException ex, HttpServletRequest request) {
        return getMapResponseEntity(request, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
