package org.deltacore.delta.domains.auth.rest;

import org.deltacore.delta.domains.auth.service.UserQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
public class AuthQuery {

    private final UserQueryService userQueryService;

    @Autowired
    public AuthQuery(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @GetMapping(path = "/get/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserUsername(@PathVariable String username) {
        return ResponseEntity
                .ok(userQueryService.getUserUsername(username));
    }
}
