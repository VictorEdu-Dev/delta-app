package org.deltacore.delta.domains.auth.service.event;

import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class AuthFailure extends AbstractAuthenticationFailureEvent {
    public AuthFailure(Authentication authentication, AuthenticationException exception) {
        super(authentication, exception);
    }
}
