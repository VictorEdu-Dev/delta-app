package org.deltacore.delta.config.security;

import org.deltacore.delta.dto.user.UserDTO;
import org.deltacore.delta.dto.user.UserDeltaMapper;
import org.deltacore.delta.model.user.User;
import org.deltacore.delta.repositorie.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserDTO userDTO = userDeltaMapper.toDTO(user);
        return new DeltaUserDetails(userDTO);
    }
}
