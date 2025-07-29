package org.deltacore.delta.domains.auth.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.deltacore.delta.domains.auth.exception.InvalidRecoveryCodeException;
import org.deltacore.delta.domains.auth.exception.InvalidTokenException;
import org.deltacore.delta.domains.auth.exception.UserNotFoundException;
import org.deltacore.delta.shared.exception.AdviceStruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AuthExceptionAdvice extends AdviceStruct {
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> handleBusiness(InvalidTokenException ex, HttpServletRequest request) {
        return getMapResponseEntity(request, "INVALID_TOKEN",
                ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
        return getMapResponseEntity(request, "",
                ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRecoveryCodeException.class)
    public ResponseEntity<Map<String, Object>> handleProfileImageNotSetException(InvalidRecoveryCodeException ex, HttpServletRequest request) {
        return getMapResponseEntity(request, "",
                ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFound(UsernameNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("Error:", ex.getMessage());
        body.put("Status:", HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("Error:", ex.getMessage());
        body.put("Status:", HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<?> handleAuthorizationDenied(AuthorizationDeniedException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("Error:", ex.getMessage());
        body.put("Status:", HttpStatus.FORBIDDEN.value());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }
}
