package org.deltacore.delta.controller.monitoring;

import org.deltacore.delta.service.monitoring.MonitoringQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("monitoring/")
public class MonitoringQuery {
    private final MonitoringQueryService monitoringQueryService;

    @Autowired
    public MonitoringQuery(MonitoringQueryService monitoringQueryService) {
        this.monitoringQueryService = monitoringQueryService;
    }

    @GetMapping(path = "get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMonitoring(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping(path = "get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMonitoring() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping(path = "subject/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findSubjectByCode(@PathVariable("code") String code) {
        return monitoringQueryService.findSubjectByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(path = "monitor/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findMonitorByUserUsername(@PathVariable("username") String username) {
        return monitoringQueryService.findMonitorByUserUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


}
