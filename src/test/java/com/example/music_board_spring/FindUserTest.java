package com.example.music_board_spring;

import com.example.music_board_spring.exception.UserNotFoundException;
import com.example.music_board_spring.model.entity.Users;
import com.example.music_board_spring.repository.UserRepository;
import com.example.music_board_spring.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class FindUserTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindUserById_Success() {
        Integer userId = 1;
        Users user = new Users();
        user.setUserId(userId);
        user.setUsername("testuser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<Users> result = userService.findById(userId);

        assertNotNull(result);
        assertEquals(userId, result.get().getUserId());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void testFindUserById_UserNotFound() {
        Integer userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(userId));
    }
}
