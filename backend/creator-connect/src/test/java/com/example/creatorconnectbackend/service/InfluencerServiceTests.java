package com.example.creatorconnectbackend.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.example.creatorconnectbackend.models.Gender;
import com.example.creatorconnectbackend.models.Influencer;
import com.example.creatorconnectbackend.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;

class InfluencerServiceTest {
	
	private Influencer rowMapper = new Influencer();

	@Mock
    private ResultSet rs;
	
    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private UserService userService;

    @Mock
    SimpleJdbcInsert jdbcInsert;
    
    @InjectMocks
    private InfluencerService influencerService;

    private Influencer createMockInfluencer(long id) {
        Influencer influencer = new Influencer();
        influencer.setInfluencerID(id);
        influencer.setName("John Doe");
        influencer.setProfileImage("https://example.com/profile-image.png");
        influencer.setGender(Gender.MALE);
        influencer.setInfluencerName("John Doe");
        influencer.setInfluencerType("Fashion");
        influencer.setMinRate(1000L);
        influencer.setPreviousBrands("Nike, Adidas, Apple");
        influencer.setLocation("Los Angeles");
        influencer.setBio("A dedicated influencer in the fashion industry.");
        influencer.setBirthdate(LocalDate.of(1992, 1, 1));
        influencer.setInstagram("john_doe");
        influencer.setTikTok("john_doe");
        influencer.setTweeter("john_doe");
        influencer.setYoutube("john_doe");
        influencer.setFacebook("john_doe");
        influencer.setTwitch("john_doe");
        influencer.setInfluencerNiche(Arrays.asList("Fashion", "Sports", "Tech"));
        influencer.setBestPosts(Arrays.asList("Post1", "Post2", "Post3"));
        
        return influencer;
    }
    
    private User createUser(String userType) {
        User user = new User();
        user.setUser_type(userType);
        // Set other user properties as needed
        return user;
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        influencerService = new InfluencerService(jdbcTemplate, userService);
        influencerService.setJdbcInsert(jdbcInsert);
    }
    
    @Test
    void testRegister_NonInfluencerUser() {
        User user = new User();
        user.setUser_type("Regular");

        Influencer influencer = createMockInfluencer(1L);

        RowMapper<User> userRowMapper = (rs, rowNum) -> user;
        when(userService.getUserRowMapper()).thenReturn(userRowMapper);
        when(jdbcTemplate.queryForObject(any(String.class), eq(new Object[]{1L}), eq(userRowMapper))).thenReturn(user);

        Influencer registeredInfluencer = influencerService.register(influencer, 1L);

        assertNull(registeredInfluencer);
    }

    
    @Test
    void testUpdate_InfluencerExists() {
        Influencer influencer = createMockInfluencer(1L);
        influencer.setName("Updated Name");

        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(influencer);

        Influencer updatedInfluencer = influencerService.update(1L, influencer);

        assertNotNull(updatedInfluencer);
        assertEquals("Updated Name", updatedInfluencer.getName());
    }
    
    @Test
    void testUpdate_InfluencerDoesNotExist() {
        Influencer influencer = createMockInfluencer(1L);

        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(0);

        assertThrows(RuntimeException.class, () -> influencerService.update(1L, influencer));
    }

    @Test
    void testGetById_ExistingId() {

        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(createMockInfluencer(1L));

        Influencer influencer = influencerService.getById(1L);
        assertNotNull(influencer);
        assertEquals(1L, influencer.getInfluencerID());
        assertEquals("John Doe", influencer.getName());
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    void testGetById_NonExistingId() {

        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenThrow(EmptyResultDataAccessException.class);

        assertThrows(RuntimeException.class, () -> influencerService.getById(1L));

        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    void testGetAll_InfluencersExist() {
        List<Influencer> influencers = new ArrayList<>();
        influencers.add(createMockInfluencer(1L));
        influencers.add(createMockInfluencer(2L));

        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(influencers);

        List<Influencer> returnedInfluencers = influencerService.getAll();

        assertNotNull(returnedInfluencers);
        assertEquals(2, returnedInfluencers.size());
    }

    @Test
    void testDeleteById() {

        InfluencerService influencerService = Mockito.spy(new InfluencerService(jdbcTemplate, userService));

        Mockito.doReturn(1).when(jdbcTemplate).update(Mockito.anyString(), Mockito.anyLong());

        assertDoesNotThrow(() -> influencerService.deleteById(1L));
        Mockito.verify(influencerService, Mockito.times(1)).deleteById(Mockito.anyLong());
    }
    
    @Test
    void testDeleteById_InfluencerNotFound() {
        // Arrange
        Long influencerId = 1L;
        InfluencerService influencerService = Mockito.spy(new InfluencerService(jdbcTemplate, userService));

        // Mock the jdbcTemplate.update method to return 0
        Mockito.doReturn(0).when(jdbcTemplate).update(Mockito.anyString(), Mockito.anyLong());

        // Act and Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> influencerService.deleteById(influencerId));
        assertEquals("Failed to delete. Influencer not found with id: " + influencerId, exception.getMessage());
        Mockito.verify(influencerService, Mockito.times(1)).deleteById(Mockito.anyLong());
    }

}

