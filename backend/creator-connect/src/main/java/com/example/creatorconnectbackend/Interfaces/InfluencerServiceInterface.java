package com.example.creatorconnectbackend.Interfaces;

import java.util.List;

import com.example.creatorconnectbackend.model.Influencer;

public interface InfluencerServiceInterface {
	Influencer register(Influencer influencer);
    Influencer getById(Long id);
    Influencer update(Long id, Influencer updatedInfluencer);
    List<Influencer> getAll();
    void deleteById(Long id);
}
