package org.deltacore.delta.controller;

import jakarta.websocket.server.PathParam;
import org.deltacore.delta.dto.UserDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
public class Home {


    @PostMapping(path = "/home/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> home(@RequestBody UserDTO user, long id) {

        if (id == 123) {
            return ResponseEntity.ok(UserDTO.builder().username(user.getUsername()).passwordHash(user.getPasswordHash()).build());
        }

        return ResponseEntity
                .ok(user);
    }

}
