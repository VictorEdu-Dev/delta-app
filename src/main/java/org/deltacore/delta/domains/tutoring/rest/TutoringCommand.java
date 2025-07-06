package org.deltacore.delta.domains.tutoring.rest;

import jakarta.validation.Valid;
import org.deltacore.delta.domains.tutoring.dto.TutoringDTO;
import org.deltacore.delta.domains.tutoring.servive.TutoringCommandService;
import org.deltacore.delta.shared.dto.OnCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> register(@RequestBody @Validated(OnCreate.class) TutoringDTO monitoring) {
        TutoringDTO tutoringDTO = monitoringCmdService.registerMonitoring(monitoring);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tutoringDTO);
    }

    @PatchMapping(
            path = "/update",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestBody @Valid TutoringDTO monitoring) {
        TutoringDTO updated = monitoringCmdService.updateMonitoring(monitoring);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/deactivate")
    public ResponseEntity<?> deactivate() {
        monitoringCmdService.deactivateTutoring();
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/reactivate")
    public ResponseEntity<?> reactivate() {
        monitoringCmdService.reactivateTutoring();
        return ResponseEntity.noContent().build();
    }

}
