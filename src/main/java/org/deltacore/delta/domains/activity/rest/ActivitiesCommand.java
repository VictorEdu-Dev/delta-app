package org.deltacore.delta.domains.activity.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.deltacore.delta.domains.activity.dto.ActivityDTO;
import org.deltacore.delta.domains.activity.dto.ActivityFilesDTO;
import org.deltacore.delta.domains.activity.servive.ActivityCreation;
import org.deltacore.delta.shared.dto.OnCreate;
import org.deltacore.delta.shared.dto.OnUpdate;
import org.deltacore.delta.domains.activity.servive.ActivitiesSectionService;
import org.deltacore.delta.domains.activity.servive.ActivityUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    private ActivityCreation activityCreation;

    @Autowired
    public ActivitiesCommand(ActivitiesSectionService activitiesService, ActivityUploadService activityUploadService) {
        this.activitiesService = activitiesService;
        this.activityUploadService = activityUploadService;
    }

    @Operation(
            summary = "Criar nova atividade",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = ActivityDTO.ActivityRegister.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Atividade criada com sucesso",
                            content = @Content(schema = @Schema(implementation = ActivityDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos")
            }
    )
    @PostMapping(value = "/create",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveActivity(@RequestBody @Valid ActivityDTO.ActivityRegister activity) {
        return ResponseEntity
                .ok(activityCreation.saveActivity(activity));
    }

    @Operation(
            summary = "Atualizar atividade existente",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = ActivityDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Atividade atualizada com sucesso",
                            content = @Content(schema = @Schema(implementation = ActivityDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Atividade não encontrada")
            }
    )
    @PutMapping(value = "/edit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateActivity(@PathVariable Long id, @RequestBody @Validated(OnUpdate.class) ActivityDTO activity) {
        return ResponseEntity
                .ok(activitiesService.updateActivity(id, activity));
    }

    @Operation(
            summary = "Excluir atividade pelo ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Atividade excluída com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Atividade não encontrada")
            }
    )
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteActivity(@PathVariable Long id) {
        activitiesService.deleteActivity(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    // Atualizações para um único ou mais campos, desde que não sejam para o recurso inteiro,
    // devem semanticamente usar PATCH como metodo HTTP mais adequado.
    @Operation(
            summary = "Marcar atividade como concluída",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Atividade marcada como concluída",
                            content = @Content(schema = @Schema(implementation = ActivityDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Atividade não encontrada")
            }
    )
    @PatchMapping(value = "/{id}/complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActivityDTO> markActivityAsCompleted(@PathVariable Long id) {
        ActivityDTO updatedActivity = activitiesService.completeActivity(id);
        return ResponseEntity.ok(updatedActivity);
    }

    @Operation(
            summary = "Upload de arquivos para uma atividade",
            description = "Permite enviar um ou mais arquivos para a atividade especificada pelo ID",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Arquivos enviados com sucesso",
                            content = @Content(schema = @Schema(implementation = ActivityFilesDTO.class))),
                    @ApiResponse(responseCode = "415", description = "Tipo de arquivo não suportado")
            }
    )
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

    @Autowired @Lazy
    private void setActivityCreation(ActivityCreation activityCreation) {
        this.activityCreation = activityCreation;
    }

}
