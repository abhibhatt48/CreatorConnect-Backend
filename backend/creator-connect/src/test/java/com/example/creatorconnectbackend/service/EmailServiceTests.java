package com.example.creatorconnectbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import static org.mockito.Mockito.*;

public class EmailServiceTests {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendOtpMessage() throws MessagingException {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        mimeMessageHelper.setTo("group2@gmail.com");
        mimeMessageHelper.setSubject("Subject");
        mimeMessageHelper.setText("Text", true);

        emailService.sendOtpMessage("group2@gmail.com", "Subject", "Text");

        verify(javaMailSender).createMimeMessage();
        verify(mimeMessageHelper).setTo("group2@gmail.com");
        verify(mimeMessageHelper).setSubject("Subject");
        verify(mimeMessageHelper).setText("Text", true);
        verify(javaMailSender).send(mimeMessage);
    }
}
