package org.deltacore.delta.service;

import org.deltacore.delta.dto.user.UserDTO;
import org.deltacore.delta.dto.user.UserDeltaMapper;
import org.deltacore.delta.exception.ResourceNotFoundException;
import org.deltacore.delta.model.user.User;
import org.deltacore.delta.repositorie.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserQueryService {

    private final UserDAO userDAO;
    private final UserDeltaMapper userDeltaMapper;

    @Autowired
    public UserQueryService(UserDAO userDAO, UserDeltaMapper userDeltaMapper) {
        this.userDAO = userDAO;
        this.userDeltaMapper = userDeltaMapper;
    }

    public Optional<UserDTO> getUserUsername(String username) {
        if(username == null) return Optional.empty();

        Optional<User> user = Optional.ofNullable(userDAO.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User not found with username: " + username)
        ));

        return user.map(userDeltaMapper::toDTO);
    }
}
