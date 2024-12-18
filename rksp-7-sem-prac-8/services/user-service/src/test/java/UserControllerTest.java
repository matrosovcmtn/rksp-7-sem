package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .id(1L)
                .username("user1")
                .password("password1")
                .build();
        user2 = User.builder()
                .id(2L)
                .username("user2")
                .password("password2")
                .build();
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("user1", response.getBody().get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUser_shouldReturnUser_whenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        ResponseEntity<Optional<User>> response = userController.getUser(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isPresent());
        assertEquals("user1", response.getBody().get().getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUser_shouldReturnNotFound_whenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Optional<User>> response = userController.getUser(1L);

        assertEquals(404, response.getStatusCodeValue());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void createUser_shouldReturnCreatedUser() {
        User newUser = User.builder()
                .id(3L)
                .username("newUser")
                .password("newPassword")
                .build();
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        ResponseEntity<User> response = userController.createUser("newUser", "newPassword");

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("newUser", response.getBody().getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }
}
