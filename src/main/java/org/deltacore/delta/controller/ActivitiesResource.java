package org.deltacore.delta.controller;

import jakarta.validation.Valid;
import org.deltacore.delta.dto.ActivityDTO;
import org.deltacore.delta.service.ActivitiesSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/activities")
public class ActivitiesResource {

    private final ActivitiesSectionService activitiesService;

    @Autowired
    public ActivitiesResource(ActivitiesSectionService activitiesService) {
        this.activitiesService = activitiesService;
    }

    @GetMapping(value = "/list-activities", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getActivities(@RequestParam(value = "search", required = false)  String search) {
        return ResponseEntity.ok(activitiesService.getLimitedActivities(search));
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveActivity(@RequestBody @Valid ActivityDTO activity, BindingResult result) {
        ActivityDTO dtoActicity = activitiesService.saveActivity(activity);

        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errorMessage);
        }

        return ResponseEntity.ok(dtoActicity);
    }



}
