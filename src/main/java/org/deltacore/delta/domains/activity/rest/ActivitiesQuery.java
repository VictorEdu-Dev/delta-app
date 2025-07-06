package org.deltacore.delta.domains.activity.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.deltacore.delta.domains.activity.dto.ActivityFilterDTO;
import org.deltacore.delta.domains.activity.model.ActivityStatus;
import org.deltacore.delta.domains.activity.model.ActivityType;
import org.deltacore.delta.domains.activity.servive.ActivitiesSectionService;
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
public class ActivitiesQuery {
    private static final int MAX_SIZE_PAGE = 50;
    private static final int MIN_SIZE_PAGE = 0;
    private static final int PAGE_DEFAULT = 0;

    private final MessageSource messageSource;
    private final ActivitiesSectionService activitiesService;

    @Autowired
    public ActivitiesQuery(MessageSource messageSource, ActivitiesSectionService activitiesService) {
        this.messageSource = messageSource;
        this.activitiesService = activitiesService;
    }

    @Operation(summary = "Pesquisar atividades pelo termo",
            description = "Pesquisa atividades cujo conteúdo ou título contenha o termo informado",
            parameters = {
                    @Parameter(name = "q", description = "Termo de pesquisa", required = true, example = "engenharia")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de atividades filtradas"),
                    @ApiResponse(responseCode = "400", description = "Parâmetro de pesquisa inválido")
            }
    )
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchActivities(@RequestParam("q") String search) {
        return ResponseEntity.ok(activitiesService.getLimitedActivities(search));
    }

    @Operation(
            summary = "Listar atividades com filtros e paginação",
            description = "Retorna uma lista paginada de atividades de acordo com os filtros aplicados",
            parameters = {
                    @Parameter(name = "page", description = "Número da página (0-baseado)", example = "0"),
                    @Parameter(name = "size", description = "Quantidade de itens por página", example = "20"),
                    @Parameter(name = "status", description = "Status da atividade", schema = @Schema(implementation = ActivityStatus.class)),
                    @Parameter(name = "activityType", description = "Categoria da atividade", schema = @Schema(implementation = ActivityType.class)),
                    @Parameter(name = "startDate", description = "Data de início da atividade (formato: yyyy-MM-dd)", example = "2025-01-01"),
                    @Parameter(name = "endDate", description = "Data de término da atividade (formato: yyyy-MM-dd)", example = "2025-12-31")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista paginada de atividades filtradas"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
            }
    )
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFilteredActivities(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @Valid @RequestBody ActivityFilterDTO filters) {

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

    @Operation(summary = "Obter atividade pelo ID",
            parameters = {
                    @Parameter(name = "id", description = "ID da atividade", required = true, example = "123")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Detalhes da atividade"),
                    @ApiResponse(responseCode = "404", description = "Atividade não encontrada")
            }
    )
    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getActivityById(@PathVariable Long id) {
        return ResponseEntity.ok(activitiesService.loadActivityData(id));
    }
}
