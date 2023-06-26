package com.example.creatorconnectbackend.service;

import org.springframework.stereotype.Service;

import com.example.creatorconnectbackend.model.User;
import com.example.creatorconnectbackend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User login(String email, String password) {
	        User user = userRepository.findByEmail(email);
	        if(user != null && user.getPassword().equals(password)) {
	            return user;
	        }
	        return null;
	    }

	public User signup(User user) {
		 if(userRepository.findByEmail(user.getEmail()) == null){
	            return userRepository.save(user);
	        }
	        return null;
	}
}
