package com.example.creatorconnectbackend.service;

import org.springframework.stereotype.Service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.creatorconnectbackend.model.User;
import com.example.creatorconnectbackend.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Service
public class UserServiceTests {

    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    public void testLoginValidCredentials() {
        // Create Mock User
        User mockUser = new User();
        mockUser.setEmail("group2@gmail.com");
        mockUser.setPassword("group2");
        when(userRepository.findByEmail("group2@gmail.com")).thenReturn(mockUser);

        // Run the method
        User result = userService.login("group2@gmail.com", "group2");
        assertEquals(mockUser, result);
    }

    @Test
    public void testLoginInvalidCredentials() {
        // Create Mock User
        when(userRepository.findByEmail("group2@gmail.com")).thenReturn(null);
        User result = userService.login("group2@gmail.com", "group2");

        assertNull(result);
    }

    @Test
    public void testSignupNewUser() {
        // Create Mock User
        User newUser = new User();
        newUser.setEmail("group2@gmail.com");
        newUser.setPassword("group2");
        when(userRepository.findByEmail("group2@gmail.com")).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User result = userService.signup(newUser);

        assertEquals(newUser, result);
    }

    @Test
    public void testSignupExistingUser() {
        // Create Mock User
        User existingUser = new User();
        existingUser.setEmail("group2@gmail.com");
        existingUser.setPassword("group2");
        when(userRepository.findByEmail("group2@gmail.com")).thenReturn(existingUser);

        User result = userService.signup(existingUser);

        assertNull(result);
    }
}

