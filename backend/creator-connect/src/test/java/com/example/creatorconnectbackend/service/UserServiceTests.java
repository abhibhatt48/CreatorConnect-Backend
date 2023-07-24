package com.example.creatorconnectbackend.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.example.creatorconnectbackend.models.User;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserServiceTest {
	
    private UserService userService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(jdbcTemplate, emailService);
    }

    @Test
    void testGetUserRowMapper() {
        // Arrange
        UserService userService = new UserService(jdbcTemplate, emailService);

        // Act
        RowMapper<User> rowMapper = userService.getUserRowMapper();

        // Assert
        assertNotNull(rowMapper);

        ResultSet resultSet = mock(ResultSet.class);
        try {
            when(resultSet.getLong("userID")).thenReturn(1L);
            when(resultSet.getString("email")).thenReturn("test@example.com");
            when(resultSet.getString("password")).thenReturn("password");
            when(resultSet.getString("user_type")).thenReturn("user");

            User user = rowMapper.mapRow(resultSet, 1);
            assertEquals(1L, user.getUserID());
            assertEquals("test@example.com", user.getEmail());
            assertEquals("password", user.getPassword());
            assertEquals("user", user.getUser_type());
        } catch (SQLException e) {
            fail("SQLException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testRegisterUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setUser_type("user");

        // Given
        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            PreparedStatementCreator creator = (PreparedStatementCreator) args[0];
            KeyHolder holder = (KeyHolder) args[1];
            try (Connection connection = mock(Connection.class);
                 PreparedStatement preparedStatement = mock(PreparedStatement.class)) {
                when(connection.prepareStatement(any(), anyInt())).thenReturn(preparedStatement);
                when(preparedStatement.execute()).thenReturn(true);
                creator.createPreparedStatement(connection);
                // assuming the primary key of the user is an integer
                Field field = GeneratedKeyHolder.class.getDeclaredField("keyList");
                field.setAccessible(true);
                List<Map<String, Object>> keyList = new ArrayList<>();
                Map<String, Object> keyMap = new HashMap<>();
                keyMap.put("SCOPE_IDENTITY()", 1L); // assuming your auto-generated ID is a Long
                keyList.add(keyMap);
                field.set(holder, keyList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).when(jdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));

        // When
        User registeredUser = userService.userRegister(user);

        // Then
        verify(jdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));
        assertEquals(1L, registeredUser.getUserID());
        assertEquals(user.getEmail(), registeredUser.getEmail());
        assertEquals(user.getPassword(), registeredUser.getPassword());
        assertEquals(user.getUser_type(), registeredUser.getUser_type());
    }


    @Test
    public void testLoginUserValid() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setUserID(1L); // Assuming this is a valid user ID

        List<User> users = new ArrayList<>();
        users.add(user);

        when(jdbcTemplate.query(any(String.class), any(RowMapper.class), any(String.class), any(String.class)))
                .thenReturn(users);

        long userID = userService.userLogin(user);

        assertEquals(user.getUserID(), userID); // We expect the user's ID to be returned

        verify(jdbcTemplate).query(any(String.class), any(RowMapper.class), any(String.class), any(String.class));
        reset(jdbcTemplate);

        List<User> emptyUsers = new ArrayList<>();

        when(jdbcTemplate.query(any(String.class), any(RowMapper.class), any(String.class), any(String.class)))
                .thenReturn(emptyUsers);

        userID = userService.userLogin(user);

        assertEquals(-1, userID); // We expect -1 to be returned when login fails
        
        verify(jdbcTemplate).query(any(String.class), any(RowMapper.class), any(String.class), any(String.class));
    }


    @Test
    public void testForgotPassword() {
        String email = "test@example.com";

        userService.forgotPassword(email);

        verify(jdbcTemplate).update(any(String.class), any(String.class), any(String.class));
        verify(emailService).sendEmail(any(String.class), any(String.class), any(String.class));
    }

    @Test
    public void testResetPassword() {
        String token = UUID.randomUUID().toString();
        String newPassword = "newPassword";

        userService.resetPassword(token, newPassword);

        verify(jdbcTemplate).update(any(String.class), any(String.class), any(String.class));
    }
}
