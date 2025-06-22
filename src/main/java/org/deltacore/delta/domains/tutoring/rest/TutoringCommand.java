package org.deltacore.delta.domains.tutoring.rest;

import jakarta.validation.Valid;
import org.deltacore.delta.domains.tutoring.dto.TutoringDTO;
import org.deltacore.delta.domains.tutoring.servive.TutoringCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/tutoring")
public class TutoringCommand {

    private final TutoringCommandService monitoringCmdService;

    @Autowired
    public TutoringCommand(TutoringCommandService monitoringCmdService) {
        this.monitoringCmdService = monitoringCmdService;
    }

    @PostMapping(
            path = "/register",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody @Valid TutoringDTO monitoring) {
        TutoringDTO tutoringDTO = monitoringCmdService.registerMonitoring(monitoring);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tutoringDTO);
    }

}
