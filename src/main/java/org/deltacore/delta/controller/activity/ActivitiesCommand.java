package org.deltacore.delta.controller.activity;

import org.deltacore.delta.dto.ActivityDTO;
import org.deltacore.delta.dto.OnCreate;
import org.deltacore.delta.dto.OnUpdate;
import org.deltacore.delta.service.ActivitiesSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<?> saveActivity(@RequestBody @Validated(OnCreate.class) ActivityDTO activity) {
        return ResponseEntity
                .ok(activitiesService.saveActivity(activity));
    }

    @PutMapping(value = "/edit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateActivity(@PathVariable Long id, @RequestBody @Validated(OnUpdate.class) ActivityDTO activity) {
        return ResponseEntity
                .ok(activitiesService.updateActivity(id, activity));
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteActivity(@PathVariable Long id) {
        activitiesService.deleteActivity(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    // Atualizações para um único ou mais campos, desde que não sejam para o recurso inteiro,
    // devem semanticamente usar PATCH como metodo HTTP mais adequado.
    @PatchMapping(value = "/{id}/complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActivityDTO> markActivityAsCompleted(@PathVariable Long id) {
        ActivityDTO updatedActivity = activitiesService.completeActivity(id);
        return ResponseEntity.ok(updatedActivity);
    }
}
