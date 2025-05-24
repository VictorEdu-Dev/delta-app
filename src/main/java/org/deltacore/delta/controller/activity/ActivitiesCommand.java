package org.deltacore.delta.controller.activity;

import jakarta.validation.Valid;
import org.deltacore.delta.dto.ActivityDTO;
import org.deltacore.delta.service.ActivitiesSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activities")
public class ActivitiesCommand {
    private final ActivitiesSectionService activitiesService;

    @Autowired
    public ActivitiesCommand(ActivitiesSectionService activitiesService) {
        this.activitiesService = activitiesService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveActivity(@RequestBody @Valid ActivityDTO activity) {
        return ResponseEntity.ok(activitiesService.saveActivity(activity));
    }

    @PutMapping(value = "/edit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateActivity(@PathVariable Long id, @RequestBody @Valid ActivityDTO activity) {
        activitiesService.updateActivity(id, activity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteActivity(@PathVariable Long id) {
        activitiesService.deleteActivity(id);
        return ResponseEntity.noContent().build();
    }
}
