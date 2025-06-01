package org.deltacore.delta.controller;

import org.deltacore.delta.service.UserQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/home")
public class HomeQuery {

    private final UserQueryService userQueryService;

    @Autowired
    public HomeQuery(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @GetMapping(path = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserUsername(@PathVariable String username) {
        return ResponseEntity
                .ok(userQueryService.getUserUsername(username));
    }
}
