package com.example.creatorconnectbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.example.creatorconnectbackend.Interfaces.UserServiceInterface;
import com.example.creatorconnectbackend.model.User;
import jakarta.mail.MessagingException;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Service
public class UserService implements UserServiceInterface {
    
    private final JdbcTemplate jdbcTemplate;
    private final EmailService emailService;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int OTP_LENGTH = 6;

    @Autowired
    @Lazy
    public UserService(JdbcTemplate jdbcTemplate, EmailService emailService) {
        this.jdbcTemplate = jdbcTemplate;
        this.emailService = emailService;
    }

    private RowMapper<User> rowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setUserID(rs.getLong("userID"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setUser_type(rs.getString("user_type"));
        return user;
    };
    
    public RowMapper<User> getUserRowMapper() {
        return rowMapper;
    }

    public User register(User user) {
        String sql = "INSERT INTO users (email, password, user_type) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getUser_type());
            return ps;
        }, keyHolder);

        Long userId = keyHolder.getKey().longValue();
        user.setUserID(userId);
        return user;
    }

    public boolean login(User user) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        List<User> users = jdbcTemplate.query(sql, rowMapper, user.getEmail(), user.getPassword());
        return !users.isEmpty();
    }

    public void forgotPassword(String email) {
        String token = UUID.randomUUID().toString();
        String sql = "UPDATE users SET reset_token = ? WHERE email = ?";
        jdbcTemplate.update(sql, token, email);

        // Send email with the reset password link.
        String resetPasswordLink = "http://localhost:8080//api/users/reset-password?token=" + token; 
        emailService.sendEmail(email, "Reset Password", "To reset your password, click the following link: " + resetPasswordLink);
    }

    public void resetPassword(String token, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE reset_token = ?";
        jdbcTemplate.update(sql, newPassword, token);
    }
}