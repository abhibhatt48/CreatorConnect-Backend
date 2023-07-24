package com.example.creatorconnectbackend.controllers;

import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.example.creatorconnectbackend.models.EmailBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.creatorconnectbackend.models.User;
import com.example.creatorconnectbackend.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	private final UserService userService;
	private final Logger logger = LoggerFactory.getLogger(UserController.class);

	public UserController(UserService userService) {
        this.userService = userService;
    }


	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@Valid @RequestBody User user, BindingResult bindingResult) {
		logger.info("Attempt to register new user.");
	    if(bindingResult.hasErrors()){
	        // convert the list of ObjectError objects into a single string
	        String errorMsg = bindingResult.getAllErrors().stream()
	            .map(ObjectError::getDefaultMessage)
	            .collect(Collectors.joining(", "));

	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
	    }
	    user.setUser_type(user.getUser_type());
	    
	    User registeredUser = userService.userRegister(user);
	    logger.info("User registered successfully with ID: {}", registeredUser.getUserID());
	    return ResponseEntity.ok("Registered successfully");
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@Valid @RequestBody User user, BindingResult bindingResult) {
		logger.info("Attempt to login user.");
	    if(bindingResult.hasErrors()){
	        // convert the list of ObjectError objects into a single string
	        String errorMsg = bindingResult.getAllErrors().stream()
	            .map(ObjectError::getDefaultMessage)
	            .collect(Collectors.joining(", "));

	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
	    }
	    long userId = userService.userLogin(user);
	    if (userId == -1) {
	        logger.info("User login failed");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
	    } else {
	        logger.info("User login successful");
	        return ResponseEntity.ok(userId);
	    }
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(@RequestBody EmailBody emailBody) {
		logger.info("Processing forgot password request for email: {}", emailBody.getEmail());
		userService.forgotPassword(emailBody.getEmail());
		return ResponseEntity.ok("Email with reset password link has been sent");
	}

	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
		String token = request.get("token");
		logger.info("Processing password reset request for token: {}", token);
		String newPassword = request.get("password");
		userService.resetPassword(token, newPassword);
		return ResponseEntity.ok("Password has been reset");
	}
}