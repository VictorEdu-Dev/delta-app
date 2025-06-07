package org.deltacore.delta.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.deltacore.delta.dto.LoginRequest;
import org.deltacore.delta.dto.user.UserDTO;
import org.deltacore.delta.config.security.JwtTokenService;
import org.deltacore.delta.service.UserCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/home")
public class HomeCmd {

    private final JwtTokenService jwtService;
    private final UserCommandService userCommandService;
    private final AuthenticationManager authManager;

    public HomeCmd(AuthenticationManager authManager,
                   UserCommandService userCommandService,
                   JwtTokenService jwtService) {
        this.authManager = authManager;
        this.userCommandService = userCommandService;
        this.jwtService = jwtService;
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
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_STUDENT");

        String token = jwtService.generateToken(authentication.getName(), role);

        return ResponseEntity.ok(Map.of("token", token));
    }
}
