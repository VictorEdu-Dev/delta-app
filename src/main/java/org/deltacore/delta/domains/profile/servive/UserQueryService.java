package org.deltacore.delta.domains.profile.servive;

import org.deltacore.delta.domains.profile.dto.UserDTO;
import org.deltacore.delta.domains.profile.dto.UserDeltaMapper;
import org.deltacore.delta.shared.exception.ResourceNotFoundException;
import org.deltacore.delta.domains.profile.model.User;
import org.deltacore.delta.domains.profile.repository.UserDAO;
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
