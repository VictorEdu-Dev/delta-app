package org.deltacore.delta.shared.exception;

public interface ErrorCode {
    String getMessageKey();

    default String getMessage() {
        return getMessageKey();
    }
}
