package org.deltacore.delta.domains.profile.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.deltacore.delta.domains.profile.dto.ProfileDTO;
import org.deltacore.delta.domains.profile.dto.TutorDTO;
import org.deltacore.delta.domains.profile.dto.UserDTO;
import org.deltacore.delta.domains.profile.servive.UserCommandService;
import org.deltacore.delta.shared.security.AuthenticatedUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountCMD {

    private UserCommandService userCommandService;
    private AuthenticatedUserProvider authenticatedUser;

    @Operation(
            summary = "Registrar um novo usuário",
            description = "Cria uma nova conta de usuário no sistema.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso", content = @Content(schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
            },
            security = @SecurityRequirement(name = "")
    )
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> register(@RequestBody @Valid UserDTO userDTO) {
        UserDTO savedUser = userCommandService.saveUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @Operation(
            summary = "Registrar um novo monitor",
            description = "Cria um novo monitor com informações adicionais de perfil.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Monitor criado com sucesso", content = @Content(schema = @Schema(implementation = TutorDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
            }
    )
    @PostMapping(value = "/register/tutor", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerTutor(@RequestBody @Valid TutorDTO tutorDTO) {
        TutorDTO savedTutor = userCommandService.saveTutor(tutorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTutor);
    }

    @Operation(
            summary = "Criar perfil do usuário autenticado",
            description = "Cria um perfil vinculado ao usuário autenticado.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Perfil criado com sucesso", content = @Content(schema = @Schema(implementation = ProfileDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content)
            }
    )
    @PostMapping(value = "/profile/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createProfile(@RequestBody @Valid ProfileDTO profileDTO) {
        ProfileDTO profile = userCommandService.createProfile(profileDTO, authenticatedUser.currentUser());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(profile);
    }

    @Operation(
            summary = "Excluir perfil do usuário autenticado",
            description = "Exclui o perfil do usuário autenticado com base no tipo de exclusão.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(name = "type", description = "Tipo de exclusão (safe-delete (Exclui perfil), total-delete (Exclui conta))", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Perfil excluído com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content)
            }
    )
    @DeleteMapping(value = "/profile/delete")
    public ResponseEntity<?> deleteProfile(@RequestParam("type") String typeDeletion) {
        userCommandService.deleteProfile(typeDeletion, authenticatedUser.currentUser());
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Operation(
            summary = "Atualizar perfil do usuário autenticado",
            description = "Atualiza os dados do perfil e do usuário logado.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso", content = @Content(schema = @Schema(implementation = ProfileDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content)
            }
    )
    @PatchMapping(value = "/profile/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfile(@RequestBody @Valid ProfileDTO.ProfileUpdateDTO profileDTO) {
        ProfileDTO updatedProfile = userCommandService.updateProfile(profileDTO, authenticatedUser.currentUser());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedProfile);
    }

    @Autowired @Lazy
    public void setUserCommandService(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @Autowired @Lazy
    public void setAuthenticatedUser(AuthenticatedUserProvider authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }
}
