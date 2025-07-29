package org.deltacore.delta.shared.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GlobalValidationExceptionHandler {
    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("errorCode", "VALIDATION_ERROR");
        body.put("message", "Erro de validação");
        body.put("path", request.getRequestURI());
        body.put("errors", getErrors(ex));

        return ResponseEntity.badRequest().body(body);
    }

    private List<Map<String, String>> getErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .map(error -> {
                    String code = extractMessageCode(error.getDefaultMessage());
                    if (code == null)
                        code = "VALIDATION_ERROR";
                    String userMessage = messageSource.getMessage(code, null, error.getDefaultMessage(), Locale.getDefault());
                    if (userMessage == null || userMessage.isBlank())
                        userMessage = error.getDefaultMessage();

                    return Map.of(
                            "field", error.getField(),
                            "errorCode", code,
                            "message", userMessage
                    );
                })
                .toList();
    }

    private String extractMessageCode(String defaultMessage) {
        if (defaultMessage == null) return null;
        Pattern pattern = Pattern.compile("\\{(.+?)}");
        Matcher matcher = pattern.matcher(defaultMessage);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    @Autowired @Lazy
    private void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
