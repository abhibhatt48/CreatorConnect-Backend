package com.example.creatorconnectbackend.controller;

import java.util.List;

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

import com.example.creatorconnectbackend.model.Influencer;
import com.example.creatorconnectbackend.service.InfluencerService;

@RestController
@RequestMapping("/api/influencers")
public class InfluencerController {
    private final InfluencerService influencerService;

    @Autowired
    public InfluencerController(InfluencerService influencerService) {
        this.influencerService = influencerService;
    }

    @PostMapping("/register/{userId}")
    public ResponseEntity<Influencer> registerInfluencer(@PathVariable Long userId, @RequestBody Influencer influencer) {
        Influencer registeredInfluencer = influencerService.register(influencer, userId);
        return new ResponseEntity<>(registeredInfluencer, HttpStatus.CREATED);       
    }

    @GetMapping("/{id}")
    public ResponseEntity<Influencer> getInfluencerById(@PathVariable("id") Long id) {
        Influencer influencer = influencerService.getById(id);
        return new ResponseEntity<>(influencer, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Influencer> updateInfluencer(@PathVariable("id") Long id, @RequestBody Influencer updatedInfluencer) {
        Influencer influencer = influencerService.update(id, updatedInfluencer);
        return new ResponseEntity<>(influencer, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Influencer>> getAllInfluencers() {
        List<Influencer> influencers = influencerService.getAll();
        return new ResponseEntity<>(influencers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInfluencerById(@PathVariable("id") Long id) {
        influencerService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
