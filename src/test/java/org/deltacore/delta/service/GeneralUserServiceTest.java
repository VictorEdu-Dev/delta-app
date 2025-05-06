package org.deltacore.delta.service;

import org.deltacore.delta.dto.UserDTO;
import org.deltacore.delta.model.User;
import org.deltacore.delta.repositorie.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.condition.JRE.JAVA_17;
import static org.junit.jupiter.api.condition.JRE.JAVA_21;
import static org.mockito.Mockito.*;

@EnabledForJreRange(min = JAVA_17, max = JAVA_21)
class GeneralUserServiceTest {

    private UserDAO userDAO;
    private GeneralUserService userService;

    @BeforeEach
    void setUp() {
        userDAO = mock(UserDAO.class);
        userService = new GeneralUserService(userDAO);
    }

    @Test
    void testSaveUserDB() {
        UserDTO dto = UserDTO.builder()
                .username("victor")
                .email("victor@email.com")
                .passwordHash("hash123")
                .createdAt(LocalDateTime.now())
                .build();

        userService.saveUserDB(dto);


        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userDAO).save(captor.capture());

        User savedUser = captor.getValue();
        assertEquals(dto.username(), savedUser.getUsername());
        assertEquals(dto.email(), savedUser.getEmail());
        assertEquals(dto.passwordHash(), savedUser.getPasswordHash());
    }

    @Test
    void testGetUserDBByUsername_found() {
        User user = User.builder()
                .username("victor")
                .email("victor@email.com")
                .passwordHash("hash123")
                .createdAt(LocalDateTime.now())
                .build();

        when(userDAO.findByUsername("victor")).thenReturn(user);

        UserDTO result = userService.getUserDBByUsername("victor");

        assertNotNull(result);
        assertEquals("victor", result.username());
        assertEquals("victor@email.com", result.email());
    }

    @Test
    void testGetUserDBByUsername_notFound() {
        when(userDAO.findByUsername("not_found")).thenReturn(null);
        UserDTO result = userService.getUserDBByUsername("not_found");
        assertNull(result);
    }

    @Test
    void testGetAllUsers() {
        List<User> mockList = Arrays.asList(
                User.builder().username("v1").email("v1@email.com").passwordHash("h1").createdAt(LocalDateTime.now()).build(),
                User.builder().username("v2").email("v2@email.com").passwordHash("h2").createdAt(LocalDateTime.now()).build()
        );

        when(userDAO.findAll()).thenReturn(mockList);

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("v1", result.get(0).username());
        assertEquals("v2", result.get(1).username());
    }
}