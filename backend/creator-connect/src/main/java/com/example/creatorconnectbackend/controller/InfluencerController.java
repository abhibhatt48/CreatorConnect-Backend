package com.example.creatorconnectbackend.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.creatorconnectbackend.models.Influencer;
import com.example.creatorconnectbackend.services.InfluencerService;

@RestController
@RequestMapping("/api/influencers")
public class InfluencerController {

	private final InfluencerService influencerService;
	private final Logger logger = LoggerFactory.getLogger(InfluencerController.class);

    @Autowired
    public InfluencerController(InfluencerService influencerService) {
        this.influencerService = influencerService;
    }

    @PostMapping("/register/{userId}")
    public ResponseEntity<Influencer> registerInfluencer(@PathVariable Long userId, @RequestBody Influencer influencer) {
    	logger.info("Attempt to register new influencer by user with ID: {}", userId);
        Influencer registeredInfluencer = influencerService.register(influencer, userId);
        logger.info("Influencer registered successfully with ID: {}", registeredInfluencer.getInfluencerID());
        return new ResponseEntity<>(registeredInfluencer, HttpStatus.CREATED);       
    }

    @GetMapping("/{id}")
    public ResponseEntity<Influencer> getInfluencerById(@PathVariable("id") Long id) {
    	logger.info("Request to get influencer with ID: {}", id);
        Influencer influencer = influencerService.getById(id);
        return new ResponseEntity<>(influencer, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Influencer> updateInfluencer(@PathVariable("id") Long id, @RequestBody Influencer updatedInfluencer) {
    	logger.info("Attempt to update influencer with ID: {}", id);
        Influencer influencer = influencerService.update(id, updatedInfluencer);
        logger.info("Influencer with ID: {} updated successfully", id);
        return new ResponseEntity<>(influencer, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Influencer>> getAllInfluencers() {
    	logger.info("Request to get all influencers");
        List<Influencer> influencers = influencerService.getAll();
        return new ResponseEntity<>(influencers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInfluencerById(@PathVariable("id") Long id) {
    	logger.info("Attempt to delete influencer with ID: {}", id);
        influencerService.deleteById(id);
        logger.info("Influencer with ID: {} deleted successfully", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
