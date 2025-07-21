package org.deltacore.delta.domains.tutoring.rest;

import org.deltacore.delta.domains.tutoring.servive.TutoringQueryService;
import org.deltacore.delta.shared.security.AuthenticatedUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tutoring")
public class TutoringQuery {
    private final TutoringQueryService tutoringQueryService;
    private AuthenticatedUserProvider authenticatedUserProvider;

    @Autowired
    public TutoringQuery(TutoringQueryService tutoringQueryService) {
        this.tutoringQueryService = tutoringQueryService;
    }

    @GetMapping(path = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTutoring(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping(path = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTutoring() {
        String monitor = authenticatedUserProvider.currentUsername();
        return ResponseEntity
                .ok(tutoringQueryService.getTutoring(monitor));
    }

    @GetMapping(path = "/subject/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findSubjectByCode(@PathVariable("code") String code) {
        return tutoringQueryService.findSubjectByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/subjects", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllSubjects() {
        return ResponseEntity.ok(tutoringQueryService.findAllSubjects());
    }

    @GetMapping(path = "/monitor/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findMonitorByUserUsername(@PathVariable("username") String username) {
        return tutoringQueryService.findMonitorByUserUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Autowired
    public void setAuthenticatedUserProvider(AuthenticatedUserProvider authenticatedUserProvider) {
        this.authenticatedUserProvider = authenticatedUserProvider;
    }
}
