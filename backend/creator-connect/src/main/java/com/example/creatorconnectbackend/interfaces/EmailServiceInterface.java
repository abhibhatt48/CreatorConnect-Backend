package com.example.creatorconnectbackend.interfaces;

import jakarta.mail.MessagingException;

public interface EmailServiceInterface {
    void sendOtpMessage(String to, String subject, String text) throws MessagingException;
}
