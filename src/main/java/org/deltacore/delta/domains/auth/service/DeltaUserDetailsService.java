package org.deltacore.delta.domains.auth.service;

import org.deltacore.delta.domains.auth.model.DeltaUserDetails;
import org.deltacore.delta.domains.profile.dto.UserDTO;
import org.deltacore.delta.domains.profile.dto.UserDeltaMapper;
import org.deltacore.delta.domains.profile.model.User;
import org.deltacore.delta.domains.profile.repository.UserDAO;
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
