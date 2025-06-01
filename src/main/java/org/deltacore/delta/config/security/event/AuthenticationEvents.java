package org.deltacore.delta.config.security.event;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEvents {

    @EventListener
    public void onFailure(AuthorizationDeniedEvent failure) {
        System.out.println("Authorization denied for: " + failure.getAuthentication());
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        System.out.println("Authentication successful for: " + success.getAuthentication().getName());
    }

    @EventListener
    public void onFailure(AuthFailure failures) {
        System.out.println("Authentication failed for: " + failures.getAuthentication().getName());
        System.out.println("Reason: " + failures.getException().getMessage());
    }
}
