package org.deltacore.delta.domains.profile.servive;

import org.deltacore.delta.domains.auth.exception.UserAlreadyExists;
import org.deltacore.delta.domains.profile.dto.*;
import org.deltacore.delta.domains.profile.exception.ConflictException;
import org.deltacore.delta.domains.profile.exception.IllegalArgumentException;
import org.deltacore.delta.domains.profile.exception.ProfileNotFoundException;
import org.deltacore.delta.domains.profile.exception.UserNotFoundException;
import org.deltacore.delta.domains.profile.model.Profile;
import org.deltacore.delta.domains.profile.model.Tutor;
import org.deltacore.delta.domains.profile.repository.ProfileDAO;
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

import java.math.BigDecimal;
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
    private ProfileMapper profileMapper;
    private ProfileDAO profileDAO;
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
                orElseThrow(() -> new UserNotFoundException("User not authenticated")).username();
        Optional<Tutor> tutor = tutorDAO.findByUserUsername(username);
        if (tutor.isPresent()) throw new ConflictException("Tutor already exists for user: " + username);
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found or invalid with username: " + username));

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

    @Transactional
    public ProfileDTO createProfile(ProfileDTO profileDTO, User user) {
        if (user.getProfile() != null)
            throw new ConflictException("User already has a profile. Please update the existing profile instead.");

        Profile p = profileMapper.toEntity(profileDTO);

        if (p.getTotalScore() == null)
            p.setTotalScore(BigDecimal.ZERO);
        if (p.getLevel() == null)
            p.setLevel(0);

        profileDAO.save(p);
        user.setProfile(p);
        userDAO.save(user);

        return profileMapper.toDTO(p);
    }

    @Transactional
    public void deleteProfile(String typeDeletion, User user) {
        switch (typeDeletion) {
            case "total-delete" -> totalDelete(user);
            case "safe-delete" -> safeDelete(user);
            default -> throw new IllegalArgumentException("Invalid delete type. Delete type should be \"total-delete\" or \"safe-delete\"");
        }
    }

    private void safeDelete(User user) {
        Profile profile = user.getProfile();
        if (profile == null)
            throw new ProfileNotFoundException("User has no profile to delete");

        user.setProfile(null);
        userDAO.save(user);

        profileDAO.delete(profile);
    }

    private void totalDelete(User user) {
        Profile profile = user.getProfile();
        if (profile == null)
            throw new ProfileNotFoundException("User has no profile to delete");

        user.setProfile(null);
        userDAO.save(user);

        profileDAO.delete(profile);
        userDAO.delete(user);
    }

    @Transactional
    public ProfileDTO updateProfile(ProfileDTO.ProfileUpdateDTO profileDTO, User user) {
        User userA = userDAO.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + user.getId()));
        Profile profile = userA.getProfile();

        if (profile == null)
            throw new ProfileNotFoundException("User has no profile to update");

        String username = profileDTO.userInfo().username().toLowerCase();
        String email = profileDTO.userInfo().email().toLowerCase();

        userDAO.findByUsername(username).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(userA.getId())) {
                throw new UserAlreadyExists("Username already exists: " + username);
            }
        });

        userA.setUsername(username);
        userA.setEmail(email);

        profileMapper.updateEntityFromDto(profileDTO, profile);

        return profileMapper.toDTO(profile);
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
    public void setProfileMapper(ProfileMapper profileMapper) {
        this.profileMapper = profileMapper;
    }

    @Autowired(required = false) @Lazy
    public void setProfileDAO(ProfileDAO profileDAO) {
        this.profileDAO = profileDAO;
    }

    @Autowired(required = false) @Lazy
    public void setUserBasicMapper(UserBasicMapper userBasicMapper) {
        this.userBasicMapper = userBasicMapper;
    }
}
