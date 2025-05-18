package org.deltacore.delta.controller.activity;

import jakarta.validation.Valid;
import org.deltacore.delta.dto.ActivityDTO;
import org.deltacore.delta.dto.ActivityFilterDTO;
import org.deltacore.delta.service.ActivitiesSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/activities")
public class ActivitiesResource {

    private final ActivitiesSectionService activitiesService;

    @Autowired
    public ActivitiesResource(ActivitiesSectionService activitiesService) {
        this.activitiesService = activitiesService;
    }

    // Barra de pesquisa de atividades
    @GetMapping(value = "/activities", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getActivities(@RequestParam(value = "search")  String search) {
        return ResponseEntity.ok(activitiesService.getLimitedActivities(search));
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getActivitiesOrderByDeadline(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size,
            @Valid @ModelAttribute ActivityFilterDTO filters) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("deadline").ascending());

        return ResponseEntity.ok(activitiesService.getActivitiesFiltered(pageable, filters));
    }

    @GetMapping(value = "/list-activities-tsdt", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getActivitiesWithTitleStatusDeadlineType() {
        return ResponseEntity.ok(activitiesService.getActivitiesWithTitleStatusTypeAndDeadline());
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveActivity(@RequestBody @Valid ActivityDTO activity) {
        return ResponseEntity.ok(activitiesService.saveActivity(activity));
    }
}
