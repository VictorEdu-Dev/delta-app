package org.deltacore.delta.service;

import org.deltacore.delta.dto.user.UserDTO;
import org.deltacore.delta.dto.user.UserDeltaMapper;
import org.deltacore.delta.exception.ResourceNotFoundException;
import org.deltacore.delta.model.user.Roles;
import org.deltacore.delta.model.user.User;
import org.deltacore.delta.repositorie.UserDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private UserDeltaMapper userDeltaMapper;

    @InjectMocks
    private UserCommandService userCommandService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    void saveUserShouldCreateNewUserWhenIdIsNull() {
        UserDTO inputDto = UserDTO.builder()
                .id(null)
                .username("username")
                .email("email@example.com")
                .passwordHash("hashedPassword")
                .build();

        User userEntity = new User();
        userEntity.setUsername("username");
        userEntity.setEmail("email@example.com");
        userEntity.setPasswordHash("hashedPassword");

        User savedUserEntity = new User();
        savedUserEntity.setId(1L);
        savedUserEntity.setUsername("username");
        savedUserEntity.setEmail("email@example.com");
        savedUserEntity.setPasswordHash("hashedPassword");

        UserDTO outputDto = UserDTO.builder()
                .id(1L)
                .username("username")
                .email("email@example.com")
                .passwordHash("hashedPassword")
                .build();

        when(userDeltaMapper.toEntity(inputDto)).thenReturn(userEntity);
        when(userDAO.save(any(User.class))).thenReturn(savedUserEntity);
        when(userDeltaMapper.toDTO(any(User.class))).thenReturn(outputDto);

        UserDTO result = userCommandService.saveUser(inputDto);

        assertEquals(outputDto, result);

        verify(userDeltaMapper).toEntity(inputDto);
        verify(userDAO).save(userCaptor.capture());
        verify(userDeltaMapper).toDTO(userCaptor.getValue());

        User capturedUser = userCaptor.getValue();
        assertEquals("username", capturedUser.getUsername());
        assertEquals("email@example.com", capturedUser.getEmail());
        assertEquals("hashedPassword", capturedUser.getPasswordHash());
    }

    @Test
    void saveUser_shouldUpdateUser_whenIdIsNotNull() {
        UserDTO inputDto = new UserDTO(1L, "updatedUser", "updated@example.com", "updatedPass", Roles.STUDENT);
        User existingUser = new User();
        existingUser.setId(1L);

        UserDTO outputDto = new UserDTO(1L, "updatedUser", "updated@example.com", "updatedPass", Roles.STUDENT);

        when(userDAO.findById(1L)).thenReturn(Optional.of(existingUser));

        doAnswer(invocation -> {
            UserDTO dto = invocation.getArgument(0);
            User user = invocation.getArgument(1);
            user.setUsername(dto.username());
            user.setEmail(dto.email());
            user.setPasswordHash(dto.passwordHash());
            return null;
        }).when(userDeltaMapper).updateEntityFromDTO(inputDto, existingUser);

        when(userDAO.save(existingUser)).thenReturn(existingUser);
        when(userDeltaMapper.toDTO(existingUser)).thenReturn(outputDto);

        UserDTO result = userCommandService.saveUser(inputDto);

        assertEquals(outputDto, result);
        verify(userDAO).findById(1L);
        verify(userDeltaMapper).updateEntityFromDTO(inputDto, existingUser);
        verify(userDAO).save(existingUser);
        verify(userDeltaMapper).toDTO(existingUser);
    }

    @Test
    void saveUser_shouldThrow_whenUserNotFound() {
        UserDTO inputDto = new UserDTO(999L, "nonexistent", "x@example.com", "pass", Roles.STUDENT);

        when(userDAO.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userCommandService.saveUser(inputDto));

        verify(userDAO).findById(999L);
        verifyNoInteractions(userDeltaMapper);
        verify(userDAO, times(1)).findById(999L);
    }

}
