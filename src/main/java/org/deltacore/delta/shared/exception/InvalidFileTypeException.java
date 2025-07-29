package org.deltacore.delta.shared.exception;

public class InvalidFileTypeException extends IllegalArgumentException {
    private static final Throwable cause = new Throwable("Invalid file type provided for upload.");

    public InvalidFileTypeException(String message) {
        super(message);
    }

    public InvalidFileTypeException(String message, Throwable cause) {
        super(message);
    }
}