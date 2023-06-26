package com.example.creatorconnectbackend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.creatorconnectbackend.model.User;
import com.example.creatorconnectbackend.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserControllerTests {

    private UserController userController;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void testSignUpValid() {
        User user = new User();
        // Mock the UserService behavior
        when(userService.signup(any(User.class))).thenReturn(user);

        ResponseEntity<User> response = userController.signUp(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testSignUpInvalid() {
        User user = new User();
        // Mock the UserService behavior
        when(userService.signup(any(User.class))).thenReturn(null);

        ResponseEntity<User> response = userController.signUp(user);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    public void testLoginValid() {
        // Mock User data
        User mockUser = mock(User.class);
        Mockito.when(mockUser.getEmail()).thenReturn("group2@gmail.com");
        Mockito.when(mockUser.getPassword()).thenReturn("group2");

        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);

        when(userService.login(any(String.class), any(String.class))).thenReturn(mockUser);

        ResponseEntity<User> response = userController.login(mockUser);

        // Check the ok response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
    }

    @Test
    public void testLoginInvalid() {
        User user = new User();
        // Mock the UserService behavior
        when(userService.login(any(String.class), any(String.class))).thenReturn(null);

        ResponseEntity<User> response = userController.login(user);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}

