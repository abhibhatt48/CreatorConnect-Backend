package com.example.creatorconnectbackend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.creatorconnectbackend.model.Influencer;
import com.example.creatorconnectbackend.service.InfluencerService;

public class InfluencerControllerTests {
    private InfluencerController influencerController;

    @Mock
    private InfluencerService influencerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        influencerController = new InfluencerController(influencerService);
    }

    @Test
    public void testRegisterInfluencer() {
        Influencer influencer = new Influencer();
        Long userId = 12345L;
        
        when(influencerService.register(influencer, userId)).thenReturn(influencer);

        ResponseEntity<Influencer> response = influencerController.registerInfluencer(userId, influencer);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(influencer, response.getBody());
    }

    @Test
    public void testGetInfluencerById() {
        Influencer influencer = new Influencer();
        Long id = 1L;
        when(influencerService.getById(id)).thenReturn(influencer);

        ResponseEntity<Influencer> response = influencerController.getInfluencerById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(influencer, response.getBody());
    }

    @Test
    public void testUpdateInfluencer() {
        Influencer influencer = new Influencer();
        Long id = 1L;
        when(influencerService.update(id, influencer)).thenReturn(influencer);

        ResponseEntity<Influencer> response = influencerController.updateInfluencer(id, influencer);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(influencer, response.getBody());
    }

    @Test
    public void testGetAllInfluencers() {
        Influencer influencer1 = new Influencer();
        Influencer influencer2 = new Influencer();
        List<Influencer> influencers = Arrays.asList(influencer1, influencer2);
        when(influencerService.getAll()).thenReturn(influencers);

        ResponseEntity<List<Influencer>> response = influencerController.getAllInfluencers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(influencers, response.getBody());
    }

    @Test
    public void testDeleteInfluencerById() {
        Long id = 1L;

        ResponseEntity<?> response = influencerController.deleteInfluencerById(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(influencerService).deleteById(id);
    }
}