package com.example.creatorconnectbackend.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.creatorconnectbackend.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserServiceTests {

    private UserService userService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(jdbcTemplate, emailService);
    }

//    @Test
//    public void testRegisterUser() {
//        User user = new User();
//        user.setEmail("test@example.com");
//        user.setPassword("password");
//        user.setUser_type("user");
//
//        userService.register(user);
//
//        verify(jdbcTemplate).update(any(String.class), any(String.class), any(String.class), any(String.class));
//    }

    @Test
    public void testLoginUserValid() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        List<User> users = new ArrayList<>();
        users.add(user);

        when(jdbcTemplate.query(any(String.class), any(RowMapper.class), any(String.class), any(String.class)))
                .thenReturn(users);

        boolean loggedIn = userService.login(user);

        assertTrue(loggedIn);
    }

    @Test
    public void testLoginUserInvalid() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(jdbcTemplate.query(any(String.class), any(RowMapper.class), any(String.class), any(String.class)))
                .thenReturn(new ArrayList<>());

        boolean loggedIn = userService.login(user);

        assertFalse(loggedIn);
    }

    @Test
    public void testForgotPassword() {
        String email = "test@example.com";

        userService.forgotPassword(email);

        verify(jdbcTemplate).update(any(String.class), any(String.class), any(String.class));
        verify(emailService).sendEmail(any(String.class), any(String.class), any(String.class));
    }

    @Test
    public void testResetPassword() {
        String token = UUID.randomUUID().toString();
        String newPassword = "newPassword";

        userService.resetPassword(token, newPassword);

        verify(jdbcTemplate).update(any(String.class), any(String.class), any(String.class));
    }
}