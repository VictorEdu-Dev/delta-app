package org.deltacore.delta.domains.profile.servive;

import org.deltacore.delta.domains.profile.dto.UserDTO;
import org.deltacore.delta.domains.profile.dto.UserDeltaMapper;
import org.deltacore.delta.shared.exception.ResourceNotFoundException;
import org.deltacore.delta.domains.profile.model.User;
import org.deltacore.delta.domains.profile.repository.UserDAO;
import org.deltacore.delta.shared.security.AuthenticatedUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public UserDTO getUserUsername(AuthenticatedUserProvider authenticatedUser) {
        if (authenticatedUser.currentUser() == null) throw new ResourceNotFoundException("User not authenticated.");
        String username = authenticatedUser.currentUsername();

        User user = userDAO.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        UserDTO dto = userDeltaMapper.toDTO(user);
        return dto
                .toBuilder()
                .passwordHash("******************")
                .build();
    }
}
