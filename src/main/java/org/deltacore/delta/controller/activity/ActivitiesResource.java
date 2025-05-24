package org.deltacore.delta.controller.activity;

import jakarta.validation.Valid;
import org.deltacore.delta.dto.ActivityDTO;
import org.deltacore.delta.dto.ActivityFilterDTO;
import org.deltacore.delta.service.ActivitiesSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activities")
public class ActivitiesResource {
    private static final int MAX_SIZE_PAGE = 50;
    private static final int MIN_SIZE_PAGE = 0;
    private static final int PAGE_DEFAULT = 0;

    private final MessageSource messageSource;
    private final ActivitiesSectionService activitiesService;

    @Autowired
    public ActivitiesResource(ActivitiesSectionService activitiesService, MessageSource messageSource) {
        this.activitiesService = activitiesService;
        this.messageSource = messageSource;
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

        if (size <= MIN_SIZE_PAGE || size > MAX_SIZE_PAGE) {
            String msg = messageSource.getMessage(
                    "error.size.invalid",
                    null,
                    LocaleContextHolder.getLocale());
            return ResponseEntity.badRequest().body(msg);
        }

        if (page < PAGE_DEFAULT) {
            String msg = messageSource.getMessage(
                    "error.page.invalid",
                    null,
                    LocaleContextHolder.getLocale());
            return ResponseEntity.badRequest().body(msg);
        }

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

    @PostMapping(value = "/edit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateActivity(@PathVariable Long id, @RequestBody @Valid ActivityDTO activity) {
        activitiesService.updateActivity(id, activity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteActivity(@PathVariable Long id) {
        activitiesService.deleteActivity(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getActivityById(@PathVariable Long id) {
        return ResponseEntity.ok(activitiesService.loadActivityData(id));
    }

}
