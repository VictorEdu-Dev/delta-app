package org.deltacore.delta.domains.auth.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.deltacore.delta.domains.auth.dto.LoginRequest;
import org.deltacore.delta.domains.auth.dto.TokenInfoDTO;
import org.deltacore.delta.domains.auth.service.JwtTokenService;
import org.deltacore.delta.domains.auth.service.AuthCmdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
@Tag(name = "Autenticação", description = "Endpoints relacionados à autenticação e tokens JWT")
public class AuthCmd {

    private JwtTokenService jwtService;
    private AuthenticationManager authManager;
    private AuthCmdService authCmdService;

    @Operation(
            summary = "Autenticar usuário",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LoginRequest.class),
                            examples = @ExampleObject(value = """
                {
                  "username": "admin@gmail.com",
                  "password": "Ab_12345@"
                }
                """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token gerado com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
            },
            security = @SecurityRequirement(name = "")
    )

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest login) {
        TokenInfoDTO token = authCmdService.getToken(login, authManager, jwtService);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(token);
    }

    @Operation(
            summary = "Atualizar token",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Token de refresh JWT",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TokenInfoDTO.RefreshTokenDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    }
                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token renovado com sucesso",
                            content = @Content(schema = @Schema(implementation = TokenInfoDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Token inválido ou expirado")
            },
            security = @SecurityRequirement(name = "")
    )
    @PostMapping(path = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> refreshToken(@RequestBody @Valid TokenInfoDTO.RefreshTokenDTO refreshToken) {
        TokenInfoDTO response = authCmdService.refresh(refreshToken.token(), jwtService);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Operation(
            summary = "Revogar token",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Token de refresh a ser revogado",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TokenInfoDTO.RefreshTokenDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    }
                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Token revogado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Token inválido")
            },
            security = @SecurityRequirement(name = "")
    )
    @PostMapping(path = "/revoke", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> revokeToken(@RequestBody @Valid TokenInfoDTO.RefreshTokenDTO refreshToken) {
        TokenInfoDTO response = authCmdService.revoke(refreshToken.token());
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(response);
    }

    @Autowired @Lazy
    public void setJwtService(JwtTokenService jwtService) {
        this.jwtService = jwtService;
    }

    @Autowired @Lazy
    public void setAuthCmdService(AuthCmdService authCmdService) {
        this.authCmdService = authCmdService;
    }

    @Autowired
    public void setAuthManager(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

}
