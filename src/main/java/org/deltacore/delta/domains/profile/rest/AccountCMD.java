package org.deltacore.delta.domains.profile.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.deltacore.delta.domains.activity.dto.ActivityFilesDTO;
import org.deltacore.delta.domains.profile.dto.ProfileDTO;
import org.deltacore.delta.domains.profile.dto.TutorDTO;
import org.deltacore.delta.domains.profile.dto.UserDTO;
import org.deltacore.delta.domains.profile.servive.ProfileImageDownloadService;
import org.deltacore.delta.domains.profile.servive.ProfileImageUploadService;
import org.deltacore.delta.domains.profile.servive.UserCommandService;
import org.deltacore.delta.shared.security.AuthenticatedUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

@RestController
@RequestMapping("/account")
public class AccountCMD {

    private UserCommandService userCommandService;
    private AuthenticatedUserProvider authenticatedUser;
    private ProfileImageUploadService profileImageUploadService;
    private ProfileImageDownloadService profileImageDownloadService;

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

    @Operation(
            summary = "Carrega uma nova imagem de perfil para o usuário",
            description = "Permite que o usuário carregue sua foto de perfil. " +
                    "Apenas formatos JPEG e PNG são aceitos, e o tamanho do arquivo não deve exceder 5 MB. " +
                    "Após o carregamento bem-sucedido, os metadados do perfil são retornados.",
            tags = {"Gerenciamento de Foto de Perfil"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Imagem de perfil carregada com sucesso. Retorna os metadados do arquivo carregado.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ActivityFilesDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Requisição Inválida: Nenhum arquivo fornecido ou o arquivo está vazio.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(example = "{\"timestamp\":\"2023-10-26T10:00:00\",\"status\":400,\"error\":\"Bad Request\",\"message\":\"Não é possível carregar um arquivo vazio.\",\"path\":\"/api/profile/image/upload-profile\"}"))
                    ),
                    @ApiResponse(
                            responseCode = "413",
                            description = "Carga Útil Muito Grande: A imagem de perfil excede o tamanho máximo permitido (5 MB).",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(example = "{\"timestamp\":\"2023-10-26T10:00:00\",\"status\":413,\"error\":\"Payload Too Large\",\"message\":\"O tamanho da imagem de perfil excede o limite de 2 MB: nome_do_arquivo.jpg.\",\"path\":\"/api/profile/image/upload-profile\"}"))
                    ),
                    @ApiResponse(
                            responseCode = "415",
                            description = "Tipo de Mídia Não Suportado: O formato de arquivo fornecido não é JPEG ou PNG.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(example = "{\"timestamp\":\"2023-10-26T10:00:00\",\"status\":415,\"error\":\"Unsupported Media Type\",\"message\":\"Somente tipos de imagem JPEG e PNG são permitidos para fotos de perfil.\",\"path\":\"/api/profile/image/upload-profile\"}"))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro Interno do Servidor: Ocorreu um erro inesperado durante o processo de upload do arquivo.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(example = "{\"timestamp\":\"2023-10-26T10:00:00\",\"status\":500,\"error\":\"Internal Server Error\",\"message\":\"Ocorreu um erro inesperado: Mensagem de erro interna.\",\"path\":\"/api/profile/image/upload-profile\"}"))
                    )
            }
    )
    @PostMapping(value = "/profile/upload-photo-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfileDTO> uploadProfileImage(@RequestParam("file") MultipartFile file) throws IOException {
        ProfileDTO uploadedFile = profileImageUploadService.uploadProfileImage(file, authenticatedUser.currentUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(uploadedFile);
    }

    @Operation(
            summary = "Download da imagem de perfil do usuário",
            description = "Retorna a imagem de perfil do usuário no formato JPEG ou PNG.",
            tags = {"Gerenciamento de Foto de Perfil"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Imagem de perfil retornada com sucesso.",
                            content = @Content(mediaType = MediaType.IMAGE_JPEG_VALUE + ", " + MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(responseCode = "404", description = "Usuário ou imagem de perfil não encontrados.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(example = "{\"timestamp\":\"2025-07-22T10:00:00\",\"status\":404,\"error\":\"Not Found\",\"message\":\"Imagem de perfil não encontrada.\",\"path\":\"/api/profile/download-photo-profile\"}"))
    ),
                    @ApiResponse(responseCode = "409", description = "A imagem de perfil com o ID fornecido não corresponde ao usuário associado a ela.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(example = "{\"timestamp\":\"2025-07-22T10:00:00\",\"status\":409,\"error\":\"Conflict\",\"message\":\"A imagem de perfil com o ID fornecido não corresponde ao usuário associado a ela.\",\"path\":\"/api/profile/download-photo-profile\"}"))
                    ),
                    @ApiResponse(responseCode = "500", description = "Erro interno inesperado.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(example = "{\"timestamp\":\"2025-07-22T10:00:00\",\"status\":500,\"error\":\"Internal Server Error\",\"message\":\"Ocorreu um erro inesperado.\",\"path\":\"/api/profile/download-photo-profile\"}"))
                    )
            }
    )
    @GetMapping(value = "/profile/download-photo-profile", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> downloadProfileImage(@RequestParam("fileName") String fileName) {
        byte[] imageData = profileImageDownloadService.downloadProfileImage(fileName, authenticatedUser.currentUser());
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
    public ResponseEntity<String> getSignedUrl(@RequestParam("fileName") String fileName) {
        URL signedUrl = profileImageDownloadService.getSignedUrlForProfileImage(fileName, authenticatedUser.currentUser());
        return ResponseEntity.ok(signedUrl.toString());
    }



    @Autowired @Lazy
    public void setUserCommandService(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @Autowired @Lazy
    public void setAuthenticatedUser(AuthenticatedUserProvider authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    @Autowired @Lazy
    public void setProfileImageUploadService(ProfileImageUploadService profileImageUploadService) {
        this.profileImageUploadService = profileImageUploadService;
    }

    @Autowired @Lazy
    public void setProfileImageDownloadService(ProfileImageDownloadService profileImageDownloadService) {
        this.profileImageDownloadService = profileImageDownloadService;
    }
}
