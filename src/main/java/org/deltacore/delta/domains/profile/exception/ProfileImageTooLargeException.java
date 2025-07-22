package org.deltacore.delta.domains.profile.exception;

import org.deltacore.delta.domains.activity.exception.LargeFileException;

public class ProfileImageTooLargeException extends LargeFileException {
    public ProfileImageTooLargeException(String message) {
        super(message);
    }

    public ProfileImageTooLargeException(String message, Throwable cause) {
        super(message, cause);
    }
}
