package org.deltacore.delta.domains.profile.exception;

public class EmptyFileException extends IllegalArgumentException {
    public EmptyFileException(String message) {
        super(message);
    }

    public EmptyFileException(String message, Throwable cause) {
        super(message);
    }
}