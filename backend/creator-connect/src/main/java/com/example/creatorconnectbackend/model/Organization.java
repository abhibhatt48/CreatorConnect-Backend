package com.example.creatorconnectbackend.model;

import javax.persistence.*;

import jakarta.persistence.Column;
import lombok.Data;

@Data
@Entity
@Table(name = "organizations")
public class Organization {

    @Id
    @Column(unique = true, name = "OrgID")
    private Long orgID;

    @Column(name = "OrgName")
    private String orgName;

    @Column(name = "ProfileImage")
    private String profileImage;

    @Column(name = "CompanyType")
    private String companyType;

    @Column(name = "Size")
    private Long size;

    @Column(name = "WebsiteLink")
    private String websiteLink;

    @Column(name = "TargetInfluencerType")
    private String targetInfluencerType;

    @Column(name = "Location")
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@OneToOne
    @JoinColumn(name = "OrgID", referencedColumnName = "UserID")
    private User user;
}