package com.example.creatorconnectbackend.model;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class Influencer {
	private Long influencerID;
    private String name;
    private String profileImage;
    private Gender gender;
    private String influencerName;
    private String influencerType;
    private String interestedIn;
    private Long minRate;
    private String previousBrands;
    private String location;
    private String bestPosts;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
	public Long getInfluencerID() {
		return influencerID;
	}
	public void setInfluencerID(Long influencerID) {
		this.influencerID = influencerID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public String getInfluencerName() {
		return influencerName;
	}
	public void setInfluencerName(String influencerName) {
		this.influencerName = influencerName;
	}
	public String getInfluencerType() {
		return influencerType;
	}
	public void setInfluencerType(String influencerType) {
		this.influencerType = influencerType;
	}
	public String getInterestedIn() {
		return interestedIn;
	}
	public void setInterestedIn(String interestedIn) {
		this.interestedIn = interestedIn;
	}
	public Long getMinRate() {
		return minRate;
	}
	public void setMinRate(Long minRate) {
		this.minRate = minRate;
	}
	public String getPreviousBrands() {
		return previousBrands;
	}
	public void setPreviousBrands(String previousBrands) {
		this.previousBrands = previousBrands;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getBestPosts() {
		return bestPosts;
	}
	public void setBestPosts(String bestPosts) {
		this.bestPosts = bestPosts;
	}
}

