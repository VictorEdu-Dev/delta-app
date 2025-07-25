package org.deltacore.delta.domains.profile.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.deltacore.delta.domains.profile.dto.TutorRequestDTO;
import org.deltacore.delta.domains.profile.servive.SubjectService;
import org.deltacore.delta.domains.profile.servive.TutorRegisterService;
import org.deltacore.delta.domains.tutoring.dto.SubjectDTO;
import org.deltacore.delta.shared.security.AuthenticatedUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/account")
@Tag(name = "Tutor", description = "Operações relacionadas à solicitação de autoria sobre monitoria de disciplina por usuários")
public class TutorCMD {
    private AuthenticatedUserProvider authenticatedUserProvider;
    private SubjectService subjectService;
    private TutorRegisterService tutorRequestService;

    @Operation(
            summary = "Solicitar registro como tutor (monitor)",
            description = """
                    Envia uma solicitação para que o usuário atual se torne tutor de uma disciplina.
                    A solicitação será avaliada posteriormente por um administrador.
                    
                    É necessário informar:
                    - O código da disciplina
                    - Um arquivo (comprovante) que será analisado

                    O retorno é um objeto com informações da solicitação criada.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitação registrada com sucesso",
                    content = @Content(schema = @Schema(implementation = TutorRequestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou arquivo ausente"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "409", description = "Usuário já possui uma solicitação pendente ou ativa"),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping(value = "/tutor/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerTutor(
            @Parameter(description = "Código da disciplina que o usuário deseja monitorar", required = true, example = "ECO0025")
            @RequestParam("subject") String subjectId,
            @Parameter(description = "Arquivo comprobatório (ex: histórico, certificado, etc.)", required = true)
            @RequestParam("file") MultipartFile file
    ) {
        TutorRequestDTO t = tutorRequestService.submitRequest(
                authenticatedUserProvider.currentUser(), subjectId.toUpperCase(), file);
        return ResponseEntity.ok(t);
    }

    @Operation(
            summary = "Listar disciplinas disponíveis para monitoria",
            description = """
                    Retorna a lista de disciplinas ativas no sistema para as quais os usuários podem solicitar monitoria.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de disciplinas retornada com sucesso",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SubjectDTO.SubjectInfoDTO.class))))
    })
    @GetMapping("/subjects")
    public ResponseEntity<?> getAvailableSubjects() {
        List<SubjectDTO.SubjectInfoDTO> subjects = subjectService.findAvailableSubjects();
        return ResponseEntity.ok(subjects);
    }

    @Autowired
    private void setSubjectService(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Autowired
    private void setTutorRequestService(TutorRegisterService tutorRequestService) {
        this.tutorRequestService = tutorRequestService;
    }

    @Autowired
    private void setAuthenticatedUserProvider(AuthenticatedUserProvider authenticatedUserProvider) {
        this.authenticatedUserProvider = authenticatedUserProvider;
    }
}
