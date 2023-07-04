package com.example.creatorconnectbackend.model;

public class Organization {
	private Long orgID;
    private String orgName;
    private String profileImage;
    private String companyType;
    private Long size;
    private String websiteLink;
    private String targetInfluencerType;
    private String location;
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
	public String getTargetInfluencerType() {
		return targetInfluencerType;
	}
	public void setTargetInfluencerType(String targetInfluencerType) {
		this.targetInfluencerType = targetInfluencerType;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
}