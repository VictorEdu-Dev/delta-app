package org.deltacore.delta.domains.activity.rest;

import org.deltacore.delta.domains.activity.servive.ActivitiesSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activities")
public class ActivitiesSummary {
    private final ActivitiesSectionService activitiesService;

    @Autowired
    public ActivitiesSummary(ActivitiesSectionService activitiesService) {
        this.activitiesService = activitiesService;
    }

    @Deprecated(
            forRemoval = true,
            since = "0.1.0"
    )
    @GetMapping(value = "/list-activities-tsdt", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getActivitiesSummary() {
        return ResponseEntity.ok(activitiesService.getActivitiesWithTitleStatusTypeAndDeadline());
    }
}
