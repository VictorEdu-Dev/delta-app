package org.deltacore.delta.shared.exception;

import lombok.Getter;

@Getter
public enum GeneralErrorCode implements ErrorCode {
    RESOURCE_NOT_FOUND("error.resource.not.found"),
    CONFLICT("error.resource.conflict"),
    FILE_TOO_LARGE("error.file.too.large"),
    EMPTY_FILE("error.empty.file"),
    INVALID_FILE_TYPE("error.invalid.file.type"),
    NO_SUCH_FILE("error.no.such.file"),
    INTERNAL_ERROR("error.internal.error");

    private final String messageKey;

    GeneralErrorCode(String messageKey) {
        this.messageKey = messageKey;
    }
}
