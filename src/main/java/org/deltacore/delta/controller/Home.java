package org.deltacore.delta.controller;

import org.deltacore.delta.dto.UserDTO;
import org.deltacore.delta.service.GeneralUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController()
public class Home {

    private final GeneralUserService generalUserService;

    @Autowired
    public Home(GeneralUserService generalUserService) {
        this.generalUserService = generalUserService;
    }

    @GetMapping(path = "/register/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> home(@PathVariable String username) {
        Optional<UserDTO> user = Optional.ofNullable(generalUserService.getUserDBByUsername(username));
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> home(@RequestBody UserDTO user) {
        try {
            generalUserService.saveUserDB(user);
            return ResponseEntity.ok(generalUserService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(null);
        }
    }

}
