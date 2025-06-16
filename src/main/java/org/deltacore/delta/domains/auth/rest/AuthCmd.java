package org.deltacore.delta.domains.auth.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.deltacore.delta.domains.auth.dto.LoginRequest;
import org.deltacore.delta.domains.profile.dto.UserDTO;
import org.deltacore.delta.domains.auth.service.JwtTokenService;
import org.deltacore.delta.domains.auth.service.UserCommandService;
import org.deltacore.delta.domains.auth.service.AuthCmdService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/home")
public class AuthCmd {

    private final JwtTokenService jwtService;
    private final UserCommandService userCommandService;
    private final AuthenticationManager authManager;
    private final AuthCmdService authCmdService;

    public AuthCmd(AuthenticationManager authManager,
                   UserCommandService userCommandService,
                   JwtTokenService jwtService, AuthCmdService authCmdService) {
        this.authManager = authManager;
        this.userCommandService = userCommandService;
        this.jwtService = jwtService;
        this.authCmdService = authCmdService;
    }

    @Operation(security = @SecurityRequirement(name = ""))
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid UserDTO userDTO) {
        UserDTO savedUser = userCommandService.saveUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @Operation(security = @SecurityRequirement(name = ""))
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        String token = authCmdService.getToken(request, authManager, jwtService);

        Map<String,Object> microInfo = new HashMap<>();
        microInfo.put("username", request.username());
        microInfo.put("token", token);

        Map<String,Object> tokenInfo = new HashMap<>();
        tokenInfo.put("token_info", microInfo);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tokenInfo);
    }


}
