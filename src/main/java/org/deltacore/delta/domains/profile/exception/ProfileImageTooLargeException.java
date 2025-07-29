package org.deltacore.delta.domains.profile.exception;

import org.deltacore.delta.shared.exception.FileTooLargeException;

public class ProfileImageTooLargeException extends FileTooLargeException {
    public ProfileImageTooLargeException(String message) {
        super(message);
    }
}
