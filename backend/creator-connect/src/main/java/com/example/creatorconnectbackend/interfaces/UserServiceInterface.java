package com.example.creatorconnectbackend.interfaces;

import com.example.creatorconnectbackend.models.User;

public interface UserServiceInterface {
	User userRegister(User user);
    long userLogin(User user);
    void forgotPassword(String email);
    void resetPassword(String token, String newPassword);
}
