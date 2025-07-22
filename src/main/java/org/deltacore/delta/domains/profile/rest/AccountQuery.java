package org.deltacore.delta.domains.profile.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.deltacore.delta.domains.profile.servive.ProfileImageDownloadService;
import org.deltacore.delta.shared.security.AuthenticatedUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;

@RestController
@RequestMapping("/account")
public class AccountQuery {
    private ProfileImageDownloadService profileImageDownloadService;
    private AuthenticatedUserProvider authenticatedUser;

    @Operation(
            summary = "Download da imagem de perfil do usuário",
            description = "Retorna a imagem de perfil do usuário no formato JPEG ou PNG.",
            tags = {"Gerenciamento de Foto de Perfil"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Imagem de perfil retornada com sucesso.",
                            content = @Content(mediaType = MediaType.IMAGE_JPEG_VALUE + ", " + MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Usuário não autenticado ou token inválido.",
                            content = @Content
                    ),
                    @ApiResponse(responseCode = "404", description = "Usuário ou imagem de perfil não encontrados.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(example = "{\"timestamp\":\"2025-07-22T10:00:00\",\"status\":404,\"error\":\"Not Found\",\"message\":\"Imagem de perfil não encontrada.\",\"path\":\"/api/profile/download-photo-profile\"}"))
                    ),
                    @ApiResponse(responseCode = "500", description = "Erro interno inesperado.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(example = "{\"timestamp\":\"2025-07-22T10:00:00\",\"status\":500,\"error\":\"Internal Server Error\",\"message\":\"Ocorreu um erro inesperado.\",\"path\":\"/api/profile/download-photo-profile\"}"))
                    )
            }
    )
    @GetMapping(value = "/profile/download-photo-profile", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> downloadProfileImage() {
        byte[] imageData = profileImageDownloadService.downloadProfileImage(authenticatedUser.currentUser());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageData);
    }

    @Operation(
            summary = "Obtém URL assinada para acesso temporário à imagem de perfil",
            description = "Gera uma URL assinada válida por 15 minutos para acesso direto à imagem de perfil armazenada no GCP Storage.",
            tags = {"Gerenciamento de Foto de Perfil"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "URL assinada gerada com sucesso.",
                            content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE + ", " + MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Usuário não autenticado ou token inválido.",
                            content = @Content
                    ),
                    @ApiResponse(responseCode = "404", description = "Usuário ou imagem de perfil não encontrados.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(example = "{\"timestamp\":\"2025-07-22T10:00:00\",\"status\":404,\"error\":\"Not Found\",\"message\":\"Imagem de perfil não encontrada.\",\"path\":\"/api/profile/photo-profile-url\"}"))
                    ),
                    @ApiResponse(responseCode = "500", description = "Erro interno inesperado.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(example = "{\"timestamp\":\"2025-07-22T10:00:00\",\"status\":500,\"error\":\"Internal Server Error\",\"message\":\"Ocorreu um erro inesperado.\",\"path\":\"/api/profile/photo-profile-url\"}"))
                    )
            }
    )
    @GetMapping("/profile/photo-profile-url")
    public ResponseEntity<String> getSignedUrl() {
        URL signedUrl = profileImageDownloadService.getSignedUrlForProfileImage(authenticatedUser.currentUser());
        return ResponseEntity.ok(signedUrl.toString());
    }

    @Autowired
    private void setProfileImageDownloadService(ProfileImageDownloadService profileImageDownloadService) {
        this.profileImageDownloadService = profileImageDownloadService;
    }

    @Autowired
    private void setAuthenticatedUser(AuthenticatedUserProvider authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }
}
