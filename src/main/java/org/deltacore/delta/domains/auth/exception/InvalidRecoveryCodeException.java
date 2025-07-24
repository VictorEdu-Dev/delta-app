package org.deltacore.delta.domains.auth.exception;

public class InvalidRecoveryCodeException extends RuntimeException {
    public InvalidRecoveryCodeException(String message) {
        super(message);
    }
}
