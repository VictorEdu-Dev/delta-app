package org.deltacore.delta.config.security;

import org.deltacore.delta.dto.user.UserDTO;
import org.deltacore.delta.dto.user.UserDeltaMapper;
import org.deltacore.delta.model.user.User;
import org.deltacore.delta.repositorie.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeltaUserDetailsService implements UserDetailsService {
    private final UserDAO userDAO;
    private final UserDeltaMapper userDeltaMapper;

    @Autowired
    public DeltaUserDetailsService(UserDAO userDAO, UserDeltaMapper userDeltaMapper) {
        this.userDeltaMapper = userDeltaMapper;
        this.userDAO = userDAO;
    }

    @Override
    public DeltaUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userDAO.findByUsername(username);

        UserDTO userDTO = userDeltaMapper.toDTONoLazyRelationship(user.orElseThrow(() ->
                new UsernameNotFoundException("User not found with username: " + username)));
        return new DeltaUserDetails(userDTO);
    }
}
