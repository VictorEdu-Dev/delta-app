package org.deltacore.delta.controller;

import org.deltacore.delta.controller.activity.ActivitiesResource;
import org.deltacore.delta.dto.ActivityDTO;
import org.deltacore.delta.service.ActivitiesSectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class ActivitiesResourceTest {

    @InjectMocks
    private ActivitiesResource activitiesResource;

    @Mock
    private ActivitiesSectionService activitiesService;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveActivityShouldReturnBadRequestWhenValidationFails() {
        FieldError fieldError = new FieldError("activityDTO", "title", "Title is required");
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));

        ActivityDTO activityDTO = new ActivityDTO(null, "", "Description", null, "", 1, null);

        doNothing().when(activitiesService).saveActivity(any(ActivityDTO.class));

        ResponseEntity<?> response = activitiesResource.saveActivity(activityDTO, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Title is required", response.getBody());
    }

    @Test
    void saveActivityShouldReturnOkWhenValidationSucceeds() {

        when(bindingResult.hasErrors()).thenReturn(false);

        ActivityDTO activityDTO = new ActivityDTO(null, "Activity Title", "Description", null, "", 1, null);

        doNothing().when(activitiesService).saveActivity(any(ActivityDTO.class));

        ResponseEntity<?> response = activitiesResource.saveActivity(activityDTO, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(activityDTO, response.getBody());
    }
}