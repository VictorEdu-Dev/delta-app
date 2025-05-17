package org.deltacore.delta.controller.activity;

import jakarta.validation.Valid;
import org.deltacore.delta.controller.APIRoutes;
import org.deltacore.delta.dto.ActivityDTO;
import org.deltacore.delta.service.ActivitiesSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(APIRoutes.BASE_API)
public class ActivitiesResource {

    private final ActivitiesSectionService activitiesService;

    @Autowired
    public ActivitiesResource(ActivitiesSectionService activitiesService) {
        this.activitiesService = activitiesService;
    }

    @GetMapping(value = APIRoutes.LIST_ACTIVITIES, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getActivities(@RequestParam(value = "search", required = false)  String search) {
        return ResponseEntity.ok(activitiesService.getLimitedActivities(search));
    }

    @GetMapping(value = APIRoutes.LIST_ACTIVITIES_WITH_TITLE_STATUS_DEADLINE_TYPE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getActivitiesWithTitleStatusDeadlineType() {
        return ResponseEntity.ok(activitiesService.getActivitiesWithTitleStatusTypeAndDeadline());
    }

    @GetMapping(value = APIRoutes.LIST_ACTIVITIES_FILTERED, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getActivitiesOrderByDeadline(@RequestParam(value = "order", required = false) String search) {
        return ResponseEntity.ok(activitiesService.getActivitiesFiltered(search));
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveActivity(@RequestBody @Valid ActivityDTO activity) {
        return ResponseEntity.ok(activitiesService.saveActivity(activity));
    }
}
