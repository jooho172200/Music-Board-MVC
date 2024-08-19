package com.example.music_board_spring;

import com.example.music_board_spring.model.dto.UserRegistrationDTO;
import com.example.music_board_spring.model.entity.Users;
import com.example.music_board_spring.repository.UserRepository;
import com.example.music_board_spring.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserServiceTest {

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



    //회원가입 테스트
    @Test
    void testRegisterUser_Success() {
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.setUsername("newuser");
        registrationDTO.setEmail("newuser@example.com");
        registrationDTO.setPassword("password123");
        registrationDTO.setProfilePicture("profile.jpg");

        Users savedUser = new Users();
        savedUser.setUsername("newuser");
        savedUser.setEmail("newuser@example.com");
        savedUser.setPasswordHash("encodedPassword");
        savedUser.setProfilePicture("profile.jpg");
        savedUser.setRoles(List.of("ROLE_USER"));

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("newuser@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(Users.class))).thenReturn(savedUser);

        Users registeredUser = userService.registerUser(registrationDTO);

        assertNotNull(registeredUser);
        assertEquals("newuser", registeredUser.getUsername());
        assertEquals("newuser@example.com", registeredUser.getEmail());
        assertEquals("encodedPassword", registeredUser.getPasswordHash());
        assertEquals("ROLE_USER", registeredUser.getRoles().get(0));

        verify(userRepository).save(any(Users.class));
    }

}