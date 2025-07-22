package org.deltacore.delta.domains.activity.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.deltacore.delta.domains.activity.dto.ActivityFilterDTO;
import org.deltacore.delta.domains.activity.model.ActivityStatus;
import org.deltacore.delta.domains.activity.model.ActivityType;
import org.deltacore.delta.domains.activity.servive.ActivitiesSectionService;
import org.deltacore.delta.domains.activity.servive.ActivityDownloadService;
import org.deltacore.delta.domains.activity.servive.ActivityQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.function.Function;

@Validated
@RestController
@RequestMapping("/activities")
public class ActivitiesQuery {
    private static final int MAX_SIZE_PAGE = 50;
    private static final int MIN_SIZE_PAGE = 0;
    private static final int PAGE_DEFAULT = 0;

    private final MessageSource messageSource;
    private final ActivitiesSectionService activitiesService;
    private ActivityQueryService activityQueryService;
    private ActivityDownloadService activityDownloadService;

    @Autowired
    public ActivitiesQuery(MessageSource messageSource, ActivitiesSectionService activitiesService) {
        this.messageSource = messageSource;
        this.activitiesService = activitiesService;
    }

    @Operation(summary = "Pesquisar atividades pelo termo",
            description = "Pesquisa atividades cujo conteúdo ou título contenha o termo informado",
            parameters = {
                    @Parameter(name = "q", description = "Termo de pesquisa", required = true, example = "atividade")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de atividades filtradas"),
                    @ApiResponse(responseCode = "400", description = "Parâmetro de pesquisa inválido")
            }
    )
    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchActivities(
            @RequestParam("q") String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @Valid @RequestBody ActivityFilterDTO filters) {

        return ResponseEntity.ok(activitiesService.getFilteredActivities(search, page, size, filters));
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
    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFilteredActivities(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @Valid @RequestBody ActivityFilterDTO filters) {

        return handlePagedRequest(page, size, filters, pageable ->
                activitiesService.getActivitiesFiltered(pageable, filters));
    }

    @PostMapping(value = "/list-miniatures", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getActivitiesMiniatures(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @Valid @RequestBody ActivityFilterDTO filters) {

        return handlePagedRequest(page, size, filters, pageable ->
                activityQueryService.getActivityMiniature(pageable, filters));
    }

    private ResponseEntity<?> handlePagedRequest(
            int page,
            int size,
            ActivityFilterDTO filters,
            Function<Pageable, Object> fetcher) {

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
        return ResponseEntity.ok(fetcher.apply(pageable));
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
    public ResponseEntity<?> getActivityById(
            @Parameter(description = "ID positivo da atividade", example = "1", required = true)
            @PathVariable
            @Positive(message = "{error.file.id.invalid}")
            Long id) {
        return ResponseEntity.ok(activitiesService.loadActivityData(id));
    }

    @Operation(summary = "Download de arquivo da atividade",
            description = "Faz o download do arquivo associado à atividade pelo ID do arquivo.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Arquivo baixado com sucesso",
                            content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE + ", " + MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "400", description = "ID inválido",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "404", description = "Arquivo não encontrado",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            })
    @GetMapping(value = "/get-file/{id}",
            produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<byte[]> download(
            @Parameter(description = "ID positivo do arquivo", example = "1", required = true)
            @PathVariable
            @Positive(message = "{error.file.id.invalid}")
            Long id) {
        byte[] data = activityDownloadService.loadActivityFile(id);

        String objectName = activityDownloadService.getObjectNameByFileId(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + objectName.substring(objectName.lastIndexOf('/') + 1) + "\"")
                .contentLength(data.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }

    @Operation(summary = "Gerar link assinado para arquivo da atividade",
            description = "Gera um link temporário para acessar o arquivo no navegador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Link assinado gerado",
                            content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE + ", " + MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "400", description = "ID inválido"),
                    @ApiResponse(responseCode = "404", description = "Arquivo não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro ao gerar link assinado")
            })
    @GetMapping(value = "/get-file-link/{id}")
    public ResponseEntity<String> link(
            @Parameter(description = "ID positivo do arquivo", example = "1", required = true)
            @PathVariable
            @Positive(message = "{error.file.id.invalid}")
            Long id) throws Exception {
        URL signedUrl = activityDownloadService.getSignedUrlByFileId(id);
        return ResponseEntity.ok(signedUrl.toString());
    }



    @Autowired @Lazy
    public void setActivityQueryService(ActivityQueryService activityQueryService) {
        this.activityQueryService = activityQueryService;
    }

    @Autowired @Lazy
    private void setActivityDownloadService(ActivityDownloadService activityDownloadService) {
        this.activityDownloadService = activityDownloadService;
    }
}
