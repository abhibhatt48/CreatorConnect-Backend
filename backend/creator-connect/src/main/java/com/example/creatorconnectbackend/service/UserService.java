package com.example.creatorconnectbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.creatorconnectbackend.interfaces.UserServiceInterface;
import com.example.creatorconnectbackend.model.OtpDetails;
import com.example.creatorconnectbackend.model.User;
import com.example.creatorconnectbackend.repository.UserRepository;

import jakarta.mail.MessagingException;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.Map;

@Service
public class UserService implements UserServiceInterface {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private EmailService emailService;
	
	private Map<String, OtpDetails> otps = new ConcurrentHashMap<>();


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
	    OtpDetails otpDetails = new OtpDetails(otp, System.currentTimeMillis());
	    otps.put(email, otpDetails);
	    sendOtpEmail(user, otpDetails);
	    // log the OTP for testing/debugging
	    System.out.println(otp);
	}

	public boolean validateOtp(String email, String otp) {
	    OtpDetails otpDetails = otps.get(email);
	    if (otpDetails != null && otpDetails.getOtp().equals(otp)) {
	        long currentTimestamp = System.currentTimeMillis();
	        long otpTimestamp = otpDetails.getTimestamp();
	        long differenceInMinutes = TimeUnit.MILLISECONDS.toMinutes(currentTimestamp - otpTimestamp);

	        if (differenceInMinutes <= 2) { // if the OTP is within the 2-minute window
	            otps.remove(email);
	            return true;
	        }
	    }
	    return false;
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

	private void sendOtpEmail(User user, OtpDetails otpDetails) {
	    String subject = "Your OTP";
	    String text = "Your OTP is: " + otpDetails.getOtp();
	    try {
	        emailService.sendOtpMessage(user.getEmail(), subject, text);
	    } catch (MessagingException e) {
	        throw new RuntimeException(e);
	    }
	}
}
