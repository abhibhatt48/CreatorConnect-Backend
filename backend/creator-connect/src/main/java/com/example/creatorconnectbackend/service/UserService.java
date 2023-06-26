package com.example.creatorconnectbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.creatorconnectbackend.model.User;
import com.example.creatorconnectbackend.repository.UserRepository;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

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

	public void forgotPassword(String email) {
	    User user = userRepository.findByEmail(email);
	    if (user == null) {
	        // throw an exception
	        throw new RuntimeException("User not found with email: " + email);
	    }
	    String otp = generateOtp();
	    otps.put(email, otp);
	    sendOtpEmail(user, otp);
	    // log the OTP for testing/debugging
	    System.out.println(otp);
	}

	public boolean validateOtp(String email, String otp) {
		String validOtp = otps.get(email);
		if (validOtp != null && validOtp.equals(otp)) {
			otps.remove(email);
			return true;
		} else {
			return false;
		}
	}

	public User resetPassword(String email, String password) {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			// throw an exception
			throw new RuntimeException("User not found with email: " + email);
		}
		user.setPassword(password);
		userRepository.save(user);
		return user;
	}

	private String generateOtp() {
		// Generate a 5 digit OTP
		return String.format("%05d", new Random().nextInt(99999));
	}

	private void sendOtpEmail(User user, String otp) {
        String subject = "Your OTP";
        String text = "Your OTP is: " + otp;
       // emailService.sendOtpMessage(user.getEmail(), subject, text);
    }
}
