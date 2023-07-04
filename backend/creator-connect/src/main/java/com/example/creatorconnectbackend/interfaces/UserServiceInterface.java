package com.example.creatorconnectbackend.Interfaces;

import com.example.creatorconnectbackend.model.User;

public interface UserServiceInterface {
	User register(User user);
    boolean login(User user);
    void forgotPassword(String email);
    void resetPassword(String token, String newPassword);
}
