package org.deltacore.delta.domains.profile.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.deltacore.delta.domains.profile.dto.TutorDTO;
import org.deltacore.delta.domains.profile.dto.UserDTO;
import org.deltacore.delta.domains.profile.servive.UserCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountCMD {

    private UserCommandService userCommandService;

    @Operation(security = @SecurityRequirement(name = ""))
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid UserDTO userDTO) {
        UserDTO savedUser = userCommandService.saveUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PostMapping("/register/tutor")
    public ResponseEntity<?> registerTutor(@RequestBody @Valid TutorDTO tutorDTO) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Tutor registration is not implemented yet.");
    }

    @Autowired
    public void setUserCommandService(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }
}
