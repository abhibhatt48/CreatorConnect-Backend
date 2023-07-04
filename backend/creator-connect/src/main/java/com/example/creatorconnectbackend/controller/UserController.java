package com.example.creatorconnectbackend.controller;

import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.creatorconnectbackend.model.EmailBody;
import com.example.creatorconnectbackend.model.User;
import com.example.creatorconnectbackend.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	private final UserService userService;

	public UserController(UserService userService) {
        this.userService = userService;
    }


	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@Valid @RequestBody User user, BindingResult bindingResult) {
	    if(bindingResult.hasErrors()){
	        // convert the list of ObjectError objects into a single string
	        String errorMsg = bindingResult.getAllErrors().stream()
	            .map(ObjectError::getDefaultMessage)
	            .collect(Collectors.joining(", "));

	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
	    }
	    User registeredUser = userService.register(user);
	    return ResponseEntity.ok("Registered successfully");
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@Valid @RequestBody User user, BindingResult bindingResult) {
	    if(bindingResult.hasErrors()){
	        // convert the list of ObjectError objects into a single string
	        String errorMsg = bindingResult.getAllErrors().stream()
	            .map(ObjectError::getDefaultMessage)
	            .collect(Collectors.joining(", "));

	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
	    }
	    boolean loggedIn = userService.login(user);
	    return loggedIn ? ResponseEntity.ok("Logged in successfully") : ResponseEntity.badRequest().build();
	}
	
	@PostMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(@RequestBody EmailBody emailBody) {
	    userService.forgotPassword(emailBody.getEmail());
	    return ResponseEntity.ok("Email with reset password link has been sent");
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
	    String token = request.get("token");
	    String newPassword = request.get("password");
	    userService.resetPassword(token, newPassword);
	    return ResponseEntity.ok("Password has been reset");
	}
}

