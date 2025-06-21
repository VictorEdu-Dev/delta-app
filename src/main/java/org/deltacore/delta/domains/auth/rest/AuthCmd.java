package org.deltacore.delta.domains.auth.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
public class AuthCmd {

    private JwtTokenService jwtService;
    private AuthenticationManager authManager;
    private AuthCmdService authCmdService;

    @Operation(security = @SecurityRequirement(name = ""))
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest login) {
        TokenInfoDTO token = authCmdService.getToken(login, authManager, jwtService);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(token);
    }

    @Operation(security = @SecurityRequirement(name = ""))
    @PostMapping(path = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> refreshToken(@RequestBody @Valid TokenInfoDTO.RefreshTokenDTO refreshToken) {
        TokenInfoDTO response = authCmdService.refresh(refreshToken.token(), jwtService);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Operation(security = @SecurityRequirement(name = ""))
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
