package com.example.creatorconnectbackend.service;

import com.example.creatorconnectbackend.model.Gender;
import com.example.creatorconnectbackend.model.Influencer;
import com.example.creatorconnectbackend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class InfluencerServiceTests {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private UserService userService;

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
        influencer.setInterestedIn("Fashion, Beauty, Travel");
        influencer.setMinRate(1000L);
        influencer.setPreviousBrands("Nike, Adidas, Apple");
        influencer.setLocation("Los Angeles");
        influencer.setBestPosts("https://example.com/post-1.jpg, https://example.com/post-2.jpg");
        return influencer;
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.influencerService = new InfluencerService(jdbcTemplate, userService);
    }

    @Test
    void testRegister() {
        InfluencerService influencerService = Mockito.spy(new InfluencerService(jdbcTemplate, userService));

        Influencer influencer = createMockInfluencer(1L);

        Mockito.doReturn(influencer).when(influencerService).register(Mockito.any(Influencer.class), Mockito.anyLong());
        Influencer registeredInfluencer = influencerService.register(influencer, 1L);

        assertNotNull(registeredInfluencer);
        assertEquals("John Doe", registeredInfluencer.getName());
        Mockito.verify(influencerService, Mockito.times(1)).register(Mockito.any(Influencer.class), Mockito.anyLong());
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
    void testUpdate() {
        InfluencerService influencerService = Mockito.spy(new InfluencerService(jdbcTemplate, userService));

        Influencer updatedInfluencer = createMockInfluencer(1L);
        updatedInfluencer.setName("Jane Smith");

        Mockito.doReturn(updatedInfluencer).when(influencerService).update(Mockito.anyLong(), Mockito.any(Influencer.class));
        Influencer updatedInfluencerResult = influencerService.update(1L, updatedInfluencer);

        assertNotNull(updatedInfluencerResult);
        assertEquals("Jane Smith", updatedInfluencerResult.getName());
        Mockito.verify(influencerService, Mockito.times(1)).update(Mockito.anyLong(), Mockito.any(Influencer.class));
    }

    @Test
    void testGetAll() {
        InfluencerService influencerService = Mockito.spy(new InfluencerService(jdbcTemplate, userService));

        // Create a list of mock influencer objects with fake data
        List<Influencer> influencers = new ArrayList<>();
        Influencer influencer1 = createMockInfluencer(1L);
        influencers.add(influencer1);

        Influencer influencer2 = createMockInfluencer(2L);
        influencer2.setName("Jane Smith");
        influencers.add(influencer2);

        Mockito.doReturn(influencers).when(influencerService).getAll();

        List<Influencer> allInfluencers = influencerService.getAll();

        assertNotNull(allInfluencers);
        assertEquals(2, allInfluencers.size());
        assertEquals("John Doe", allInfluencers.get(0).getName());
        assertEquals("Jane Smith", allInfluencers.get(1).getName());
        Mockito.verify(influencerService, Mockito.times(1)).getAll();
    }

    @Test
    void testDeleteById() {

        InfluencerService influencerService = Mockito.spy(new InfluencerService(jdbcTemplate, userService));

        Mockito.doReturn(1).when(jdbcTemplate).update(Mockito.anyString(), Mockito.anyLong());

        assertDoesNotThrow(() -> influencerService.deleteById(1L));
        Mockito.verify(influencerService, Mockito.times(1)).deleteById(Mockito.anyLong());
    }


}
