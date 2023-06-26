package com.example.creatorconnectbackend.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.creatorconnectbackend.model.User;
import com.example.creatorconnectbackend.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}


	@PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody User user) {
        User newUser = userService.signup(user);
        return newUser != null ? 
            ResponseEntity.ok(newUser) : ResponseEntity.unprocessableEntity().build();
        
    }
	
	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody User user) {
	    User loggedUser = userService.login(user.getEmail(), user.getPassword());
	    return loggedUser != null ? 
	        ResponseEntity.ok(loggedUser) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
	
	@PostMapping("/forgotpassword")
	public ResponseEntity<Void> forgotPassword(@RequestBody String email) {
	    try {
	        userService.forgotPassword(email);
	        return ResponseEntity.ok().build();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	@PostMapping("/validateotp")
	public ResponseEntity<Void> validateOtp(@RequestBody Map<String, String> request) {
	    String email = request.get("email");
	    String otp = request.get("otp");
	    boolean isOtpValid = userService.validateOtp(email, otp);
	    if (isOtpValid) {
	        return ResponseEntity.ok().build();
	    } else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	    }
	}

	@PostMapping("/resetpassword")
	public ResponseEntity<User> resetPassword(@RequestBody Map<String, String> request) {
	    String email = request.get("email");
	    String password = request.get("password");
	    User user = userService.resetPassword(email, password);
	    return user != null ? 
	        ResponseEntity.ok(user) : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
}
