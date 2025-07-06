package org.deltacore.delta.domains.profile.servive;

import org.deltacore.delta.domains.auth.exception.UserAlreadyExists;
import org.deltacore.delta.domains.profile.dto.*;
import org.deltacore.delta.domains.profile.exception.ConflictException;
import org.deltacore.delta.domains.profile.exception.UserNotFound;
import org.deltacore.delta.domains.profile.model.Tutor;
import org.deltacore.delta.domains.profile.repository.TutorDAO;
import org.deltacore.delta.shared.exception.ResourceNotFoundException;
import org.deltacore.delta.domains.profile.model.Roles;
import org.deltacore.delta.domains.profile.model.User;
import org.deltacore.delta.domains.profile.repository.UserDAO;
import org.deltacore.delta.shared.security.AuthenticatedUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserCommandService {

    private final UserDAO userDAO;
    private final UserDeltaMapper userDeltaMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private TutorDAO tutorDAO;
    private TutorMapper tutorMapper;
    private UserBasicMapper userBasicMapper;
    private AuthenticatedUserProvider authenticatedUser;

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
        String username = userDTO.username().toLowerCase();
        User user = userDAO.findByUsername(username).orElse(null);
        if (user != null && userDTO.id() == null)
            throw new UserAlreadyExists(username);

        boolean isNewUser = userDTO.id() == null || userDTO.id() == 0;

        if (isNewUser) {
            String encodedPassword = bCryptPasswordEncoder.encode(userDTO.passwordHash());
            UserDTO newUserDTO = userDTO
                    .toBuilder()
                    .username(username)
                    .passwordHash(encodedPassword)
                    .build();
            user = userDeltaMapper.toEntity(newUserDTO);
            user.setCreatedAt(LocalDateTime.now());
            user.setRole(Roles.STUDENT);
        } else {
            user = userDAO.findById(userDTO.id())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userDTO.id()));
            userDeltaMapper.updateEntityFromDTO(userDTO, user);
        }

        userDAO.save(user);

        return userDeltaMapper.toDTO(user).toBuilder()
                .passwordHash("***************")
                .build();
    }

    @Transactional
    public TutorDTO saveTutor(TutorDTO tutorDTO) {
        String username = authenticatedUser.current().
                orElseThrow(() -> new UserNotFound("User not authenticated")).username();
        Optional<Tutor> tutor = tutorDAO.findByUserUsername(username);
        if (tutor.isPresent()) throw new ConflictException("Tutor already exists for user: " + username);
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new UserNotFound("User not found or invalid with username: " + username));

        user.setRole(Roles.MONITOR);
        UserBasicDTO userBasicDTO = userBasicMapper.toDTO(user);

        Tutor savedTutor = tutorMapper.toEntity(tutorDTO);
        savedTutor.setUserMonitor(user);
        savedTutor.setActive(true);
        tutorDAO.save(savedTutor);

        TutorDTO savedTutorDTO = tutorMapper.toDTO(savedTutor);
        return savedTutorDTO.toBuilder()
                .userMonitor(userBasicDTO)
                .build();
    }

    @Autowired(required = false) @Lazy
    public void setTutorDAO(TutorDAO tutorDAO) {
        this.tutorDAO = tutorDAO;
    }

    @Autowired(required = false) @Lazy
    public void setTutorMapper(TutorMapper tutorMapper) {
        this.tutorMapper = tutorMapper;
    }

    @Autowired(required = false) @Lazy
    public void setAuthenticatedUser(AuthenticatedUserProvider authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    @Autowired(required = false) @Lazy
    public void setUserBasicMapper(UserBasicMapper userBasicMapper) {
        this.userBasicMapper = userBasicMapper;
    }
}
