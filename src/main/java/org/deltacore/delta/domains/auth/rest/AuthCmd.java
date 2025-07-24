package org.deltacore.delta.domains.auth.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.deltacore.delta.domains.auth.dto.*;
import org.deltacore.delta.domains.auth.dto.LoginRequest.LoginChangePasswordRequest;
import org.deltacore.delta.domains.auth.model.RecoveryCode;
import org.deltacore.delta.domains.auth.service.JwtTokenService;
import org.deltacore.delta.domains.auth.service.AuthCmdService;
import org.deltacore.delta.shared.security.AuthenticatedUserProvider;
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
    private AuthenticatedUserProvider authenticatedUser;

    @Operation(
            summary = "Autenticar usuário",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LoginRequest.class),
                            examples = @ExampleObject(value = """
                {
                  "username": "andrezaminhaex123",
                  "password": "Aa_12345@"
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
            summary = "Alterar senha do usuário",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LoginChangePasswordRequest.class),
                            examples = @ExampleObject(value = """
                {
                  "currentPassword": "Aa_12345@",
                  "newPassword": "Aa_12345@_new"
                }
                """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Senha alterada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos")
            }
    )
    @PatchMapping(value = "/change-password", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(@RequestBody @Valid LoginChangePasswordRequest login) {
        authCmdService.changePassword(login, authManager, authenticatedUser);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Operation(
            summary = "Solicitar código para recuperação de senha",
            description = "Gera e envia um código de recuperação de 6 dígitos para o e-mail do usuário. O código expira em 5 minutos.",
            tags = "Recuperação de Senha",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ForgotPasswordRequest.class),
                            examples = @ExampleObject(value = """
                            {
                              "email": "victorgostosao@email.com"
                            }
                        """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Código enviado para o e-mail com sucesso"),
                    @ApiResponse(responseCode = "400", description = "E-mail inválido ou não cadastrado")
            }
    )
    @PostMapping(value = "/forgot-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        authCmdService.sendRecoveryCode(request, RecoveryCode.Reason.FORGOT_PASSWORD);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Validar código de recuperação",
            description = "Valida o código de recuperação enviado para o e-mail do usuário. Se for válido e ainda estiver no prazo, permite redefinir a senha.",
            tags = "Recuperação de Senha",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = VerifyCodeRequest.class),
                            examples = @ExampleObject(value = """
                            {
                              "email": "jgcleite@email.com",
                              "code": "123456"
                            }
                        """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Código válido"),
                    @ApiResponse(responseCode = "400", description = "Código inválido ou expirado")
            }
    )
    @PostMapping(value = "/verify-code", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> verifyCode(@RequestBody @Valid VerifyCodeRequest request) {
        boolean valid = authCmdService.verifyRecoveryCode(
                request
                        .email()
                        .replaceAll("\\s+", "")
                        .toLowerCase(),
                request.code(), RecoveryCode.Reason.FORGOT_PASSWORD);
        if (valid) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired code.");
        }
    }

    @Operation(
            summary = "Redefinir senha",
            description = "Redefine a senha de um usuário utilizando um código de recuperação válido previamente enviado por e-mail.",
            tags = "Recuperação de Senha",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PasswordResetRequest.class),
                            examples = @ExampleObject(value = """
                            {
                              "email": "vander@eemail.com",
                              "code": "123456",
                              "newPassword": "NovaSenhaSegura@123"
                            }
                        """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Senha redefinida com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Código inválido, expirado ou dados inconsistentes")
            }
    )
    @PostMapping(value = "/reset-password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resetPassword(@RequestBody @Valid PasswordResetRequest request) {
        authCmdService.resetPassword(request
                .email()
                .replaceAll("\\s+", "")
                .toLowerCase(), request.code(), request.newPassword());
        return ResponseEntity.ok().build();
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

    @Autowired @Lazy
    private void setAuthenticatedUser(AuthenticatedUserProvider authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

}
