package com.example.creatorconnectbackend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.creatorconnectbackend.models.EmailBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.example.creatorconnectbackend.models.User;
import com.example.creatorconnectbackend.services.UserService;

public class UserControllerTest {

	private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService);
    }

    @Test
    public void testRegisterUserValid() {
        User user = new User();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.userRegister(user)).thenReturn(user);

        ResponseEntity<String> response = userController.registerUser(user, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Registered successfully", response.getBody());
    }

    @Test
    public void testRegisterUserInvalid() {
        User user = new User();
        List<ObjectError> errors = new ArrayList<>();
        errors.add(new ObjectError("user", "Email is required"));
        errors.add(new ObjectError("user", "Password must be at least 6 characters"));
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(errors);

        ResponseEntity<String> response = userController.registerUser(user, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email is required, Password must be at least 6 characters", response.getBody());
    }
    
    @Test
    public void testLoginUserValid() {
        User user = new User();
        user.setUserID(1L);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.userLogin(user)).thenReturn(user.getUserID());

        ResponseEntity<?> response = userController.loginUser(user, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.getUserID(), response.getBody());
    }

    @Test
    public void testLoginUserInvalid() {
        User user = new User();
        List<ObjectError> errors = new ArrayList<>();
        errors.add(new ObjectError("user", "Email is required"));
        errors.add(new ObjectError("user", "Password is required"));
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(errors);

        ResponseEntity<?> response = userController.loginUser(user, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        String errorMsg = errors.stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
        assertEquals(errorMsg, response.getBody());
    }

    @Test
    public void testLoginUserNotFound() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.userLogin(user)).thenReturn(-1L);

        ResponseEntity<?> response = userController.loginUser(user, bindingResult);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid email or password", response.getBody());
    }
    
    @Test
    public void testForgotPassword() {
        EmailBody emailBody = new EmailBody();
        emailBody.setEmail("test@example.com");

        ResponseEntity<String> response = userController.forgotPassword(emailBody);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Email with reset password link has been sent", response.getBody());

        verify(userService).forgotPassword(emailBody.getEmail());
    }

    @Test
    public void testResetPassword() {
        String token = "testToken";
        String newPassword = "newPassword";
        Map<String, String> request = new HashMap<>();
        request.put("token", token);
        request.put("password", newPassword);

        ResponseEntity<String> response = userController.resetPassword(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password has been reset", response.getBody());

        verify(userService).resetPassword(token, newPassword);
    }
}
