package com.example.creatorconnectbackend.model;

public class SocialMedia {
	private Long smID;
    private Long userID;
    private String platform;
    private String link;
	public Long getSmID() {
		return smID;
	}
	public void setSmID(Long smID) {
		this.smID = smID;
	}
	public Long getUserID() {
		return userID;
	}
	public void setUserID(Long userID) {
		this.userID = userID;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
}
