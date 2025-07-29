package org.deltacore.delta.shared.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.deltacore.delta.shared.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.NoSuchFileException;
import java.util.Map;

@RestControllerAdvice
public final class SharedExceptionHandler extends AdviceStruct {
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleBusiness(ConflictException ex, HttpServletRequest request) {
        return getMapResponseEntity(request, GeneralErrorCode.CONFLICT.name(), ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<?> handleEmptyFileException(EmptyFileException ex, HttpServletRequest request) {
        return getMapResponseEntity(request, GeneralErrorCode.EMPTY_FILE.name(),
                ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileTooLargeException.class)
    public ResponseEntity<?> handleFileTooLargeException(FileTooLargeException ex, HttpServletRequest request) {
        return getMapResponseEntity(request, GeneralErrorCode.FILE_TOO_LARGE.name(),
                ex.getMessage(), HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<?> handleInvalidFileTypeException(InvalidFileTypeException ex, HttpServletRequest request) {
        return getMapResponseEntity(request, GeneralErrorCode.INVALID_FILE_TYPE.name(),
                ex.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(NoSuchFileException.class)
    public ResponseEntity<?> handleNoSuchFileException(NoSuchFileException ex, HttpServletRequest request) {
        return getMapResponseEntity(request, GeneralErrorCode.NO_SUCH_FILE.name(),
                ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFoundException(NoResourceFoundException ex, HttpServletRequest request) {
        return getMapResponseEntity(request, GeneralErrorCode.RESOURCE_NOT_FOUND.name(),
                ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        return getMapResponseEntity(request, GeneralErrorCode.RESOURCE_NOT_FOUND.name(),
                ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        return getMapResponseEntity(request, GeneralErrorCode.FILE_TOO_LARGE.name(),
                ex.getMessage(), HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex, HttpServletRequest request) {
        return getMapResponseEntity(request, GeneralErrorCode.INTERNAL_ERROR.name(),
                ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
