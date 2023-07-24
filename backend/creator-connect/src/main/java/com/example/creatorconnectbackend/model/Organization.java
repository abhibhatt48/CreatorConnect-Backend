package com.example.creatorconnectbackend.models;

import java.util.List;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Organization {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orgID;
	private Long userId; 
    private String orgName;
    private String profileImage;
    private String companyType;
    private Long size;
    private String websiteLink;
    private String location;
    private List<String> targetInfluencerNiche;
    private String bio;
    private String instagram;
    private String facebook;
    private String twitter;
    private String tiktok;
    private String youtube;
    private String twitch;
    
	public Long getOrgID() {
		return orgID;
	}
	public void setOrgID(Long orgID) {
		this.orgID = orgID;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	public String getCompanyType() {
		return companyType;
	}
	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public String getWebsiteLink() {
		return websiteLink;
	}
	public void setWebsiteLink(String websiteLink) {
		this.websiteLink = websiteLink;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
	public List<String> getTargetInfluencerNiche() {
		return targetInfluencerNiche;
	}
	public void setTargetInfluencerNiche(List<String> targetInfluencerNiche) {
		this.targetInfluencerNiche = targetInfluencerNiche;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	public String getInstagram() {
		return instagram;
	}
	public void setInstagram(String instagram) {
		this.instagram = instagram;
	}
	public String getFacebook() {
		return facebook;
	}
	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}
	public String getTwitter() {
		return twitter;
	}
	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}
	public String getTiktok() {
		return tiktok;
	}
	public void setTiktok(String tiktok) {
		this.tiktok = tiktok;
	}
	public String getYoutube() {
		return youtube;
	}
	public void setYoutube(String youtube) {
		this.youtube = youtube;
	}
	public String getTwitch() {
		return twitch;
	}
	public void setTwitch(String twitch) {
		this.twitch = twitch;
	}
}