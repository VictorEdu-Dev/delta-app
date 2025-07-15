package org.deltacore.delta.domains.profile.rest;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(security = @SecurityRequirement(name = ""))
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> register(@RequestBody @Valid UserDTO userDTO) {
        UserDTO savedUser = userCommandService.saveUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PostMapping(value = "/register/tutor", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerTutor(@RequestBody @Valid TutorDTO tutorDTO) {
        TutorDTO savedTutor = userCommandService.saveTutor(tutorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTutor);
    }

    @PostMapping(value = "/profile/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createProfile(@RequestBody @Valid ProfileDTO profileDTO) {
        ProfileDTO profile = userCommandService.createProfile(profileDTO, authenticatedUser.currentUser());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(profile);
    }

    @DeleteMapping(value = "/profile/delete")
    public ResponseEntity<?> deleteProfile(@RequestParam("type") String typeDeletion) {
        userCommandService.deleteProfile(typeDeletion, authenticatedUser.currentUser());
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PatchMapping(value = "/profile/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfile(@RequestBody @Valid ProfileDTO profileDTO) {
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
