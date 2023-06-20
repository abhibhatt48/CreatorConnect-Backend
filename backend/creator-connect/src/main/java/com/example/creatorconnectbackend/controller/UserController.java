package com.example.creatorconnectbackend.controller;

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
}
