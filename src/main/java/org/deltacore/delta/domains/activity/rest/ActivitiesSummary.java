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
}
