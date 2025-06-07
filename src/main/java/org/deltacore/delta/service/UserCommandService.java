package org.deltacore.delta.service;

import org.deltacore.delta.dto.user.UserDTO;
import org.deltacore.delta.dto.user.UserDeltaMapper;
import org.deltacore.delta.exception.ResourceNotFoundException;
import org.deltacore.delta.model.user.Roles;
import org.deltacore.delta.model.user.User;
import org.deltacore.delta.repositorie.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserCommandService {

    private final UserDAO userDAO;
    private final UserDeltaMapper userDeltaMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserCommandService(
            UserDAO userDAO,
            UserDeltaMapper userDeltaMapper,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDAO = userDAO;
        this.userDeltaMapper = userDeltaMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UserDTO saveUser(UserDTO userDTO) {
        User user;
        if(userDTO.id() == null || userDTO.id() == 0) {
            String encodedPassword = bCryptPasswordEncoder.encode(userDTO.passwordHash());
            userDTO = userDTO
                    .toBuilder()
                    .passwordHash(encodedPassword)
                    .build();
            user = userDeltaMapper.toEntity(userDTO);
        } else {
            UserDTO finalUserDTO = userDTO;
            user = userDAO.findById(userDTO.id())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + finalUserDTO.id()));
            userDeltaMapper.updateEntityFromDTO(userDTO, user);
        }
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(Roles.STUDENT);
        userDAO.save(user);

        UserDTO userDTOSaved = userDeltaMapper.toDTO(user);
        return userDTOSaved
                .toBuilder()
                .passwordHash("********")
                .build();
    }
}
