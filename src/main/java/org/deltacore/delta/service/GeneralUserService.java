package org.deltacore.delta.service;

import org.deltacore.delta.dto.UserDTO;
import org.deltacore.delta.model.user.User;
import org.deltacore.delta.repositorie.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GeneralUserService {
    private final UserDAO userDAO;

    @Autowired
    public GeneralUserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void saveUserDB(UserDTO user) {
        User userToSave = User.builder()
                .username(user.username())
                .email(user.email())
                .passwordHash(user.passwordHash())
                .build();
        userDAO.save(userToSave);
    }

    public UserDTO getUserDBByUsername(String username) {
        Optional<User> user = Optional.ofNullable(userDAO.findByUsername(username));
        return user.map(value -> UserDTO.builder()
                .username(value.getUsername())
                .passwordHash(value.getPasswordHash())
                .email(value.getEmail())
                .build()).orElse(null);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userDAO.findAll();
        return users.stream()
                .map(user -> UserDTO.builder()
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .passwordHash(user.getPasswordHash())
                        .build())
                .collect(Collectors.toList());
    }
}
