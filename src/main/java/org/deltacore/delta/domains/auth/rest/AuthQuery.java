package org.deltacore.delta.domains.auth.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.deltacore.delta.domains.profile.servive.UserQueryService;
import org.deltacore.delta.shared.security.AuthenticatedUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
@Tag(name = "Autenticação", description = "Operações relacionadas ao usuário autenticado")
public class AuthQuery {

    private AuthenticatedUserProvider authenticatedUser;
    private final UserQueryService userQueryService;

    @Autowired
    public AuthQuery(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @Operation(
            summary = "Obter informações do usuário autenticado",
            description = "Retorna o nome de usuário (username) do usuário atualmente autenticado, email, informações de perfil e nome completo.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Informações do usuário retornadas com sucesso",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Usuário não autenticado",
                            content = @Content
                    )
            }
    )
    @GetMapping(path = "/get-user-info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserUsername() {
        return ResponseEntity
                .ok(userQueryService.getUserUsername(authenticatedUser));
    }

    @Autowired
    public void setAuthenticatedUser(AuthenticatedUserProvider authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }
}
