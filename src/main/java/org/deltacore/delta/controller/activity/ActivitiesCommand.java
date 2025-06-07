package org.deltacore.delta.controller.activity;

import org.deltacore.delta.dto.ActivityDTO;
import org.deltacore.delta.dto.ActivityFilesDTO;
import org.deltacore.delta.dto.OnCreate;
import org.deltacore.delta.dto.OnUpdate;
import org.deltacore.delta.service.ActivitiesSectionService;
import org.deltacore.delta.service.ActivityUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/activities/monitor")
public class ActivitiesCommand {
    private final ActivitiesSectionService activitiesService;
    private final ActivityUploadService activityUploadService;

    @Autowired
    public ActivitiesCommand(ActivitiesSectionService activitiesService, ActivityUploadService activityUploadService) {
        this.activitiesService = activitiesService;
        this.activityUploadService = activityUploadService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveActivity(@RequestBody @Validated(OnCreate.class) ActivityDTO activity,
                                          MultipartFile[] files) throws IOException {
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

    @PostMapping(value = "/files/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadFiles(@RequestParam("file") MultipartFile[] files, @PathVariable Long id) throws IOException {

        for (MultipartFile file : files) {
            if(file.isEmpty()) continue;
            String type = file.getContentType();
            if (type == null || !FileType.contains(type)) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body("Unsupported file type: " + file.getOriginalFilename());
            }
        }

        List<ActivityFilesDTO> metadataList = activityUploadService.uploadAndSaveFiles(files, id);
        return ResponseEntity.ok(metadataList);
    }

}
