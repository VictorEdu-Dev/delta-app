package org.deltacore.delta.domains.activity.exception;

public class LargeFileException extends RuntimeException {
    public LargeFileException(String message) {
        super(message);
    }

    public LargeFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public LargeFileException(Throwable cause) {
        super(cause);
    }
}
