package com.example.creatorconnectbackend.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import com.example.creatorconnectbackend.interfaces.OrganizationServiceInterface;
import com.example.creatorconnectbackend.models.Organization;
import com.example.creatorconnectbackend.models.User;

@Service
public class OrganizationService implements OrganizationServiceInterface {
    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(OrganizationService.class);
    
    
    @Autowired
    private UserService userService;


    public OrganizationService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Organization> rowMapper = (rs, rowNum) -> {
        Organization organization = new Organization();
        organization.setOrgID(rs.getLong("orgID"));
        organization.setOrgName(rs.getString("orgName"));
        organization.setProfileImage(rs.getString("profileImage"));
        organization.setCompanyType(rs.getString("companyType"));
        organization.setSize(rs.getLong("size"));
        organization.setWebsiteLink(rs.getString("websiteLink"));
        organization.setTargetInfluencerNiche(new ArrayList<>(Arrays.asList(rs.getString("targetInfluencerNiche").split(","))));
        organization.setLocation(rs.getString("location"));
        organization.setBio(rs.getString("bio"));
        organization.setInstagram(rs.getString("instagram"));
        organization.setFacebook(rs.getString("facebook"));
        organization.setTwitter(rs.getString("twitter"));
        organization.setTiktok(rs.getString("tiktok"));
        organization.setYoutube(rs.getString("youtube"));
        organization.setTwitch(rs.getString("twitch"));
        return organization;
    };

    public Organization register(Organization organization, Long userId) {
    	logger.info("Attempting to register organization with userId {}", userId);
    	// Fetch the user using the provided userId
        User user = jdbcTemplate.queryForObject("SELECT * FROM users WHERE UserID = ?", new Object[]{userId}, userService.getUserRowMapper());
        
        if (user != null && user.getUser_type().equals("Organization")) {
        	SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        	jdbcInsert.withTableName("organizations");
        	
        	Map<String, Object> parameters = new HashMap<String, Object>();
        	parameters.put("orgID", userId); 
            parameters.put("orgName", organization.getOrgName());
            parameters.put("profileImage", organization.getProfileImage());
            parameters.put("companyType", organization.getCompanyType());
            parameters.put("size", organization.getSize());
            parameters.put("websiteLink", organization.getWebsiteLink());
            parameters.put("location", organization.getLocation());
            parameters.put("targetInfluencerNiche", String.join(",", organization.getTargetInfluencerNiche()));
            parameters.put("bio", organization.getBio());
            parameters.put("instagram", organization.getInstagram());
            parameters.put("facebook", organization.getFacebook());
            parameters.put("twitter", organization.getTwitter());
            parameters.put("tiktok", organization.getTiktok());
            parameters.put("youtube", organization.getYoutube());
            parameters.put("twitch", organization.getTwitch());
            
            jdbcInsert.execute(parameters);
            
            return organization;
        } else {
        	return null;
        }
    }

    
    public Organization getById(Long id) {
        String sql = "SELECT * FROM organizations WHERE orgID = ?";
        logger.info("Attempting to get organization by id {}", id);
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
        } catch (EmptyResultDataAccessException e) {
        	logger.error("Organization not found with id: {}", id, e);
            throw new RuntimeException("Organization not found with id: " + id);
        }
    }

    public Organization update(Long id, Organization updatedOrganization) {
    	String sql = "UPDATE organizations SET orgName = ?, profileImage = ?, companyType = ?, size = ?, websiteLink = ?, targetInfluencerNiche = ?, location = ?, bio = ?, instagram = ?, facebook = ?, twitter = ?, tiktok = ?, youtube = ?, twitch = ? WHERE orgID = ?";
        logger.info("Attempting to update organization with id {}", id);
        int updated = jdbcTemplate.update(sql, updatedOrganization.getOrgName(), updatedOrganization.getProfileImage(), updatedOrganization.getCompanyType(), updatedOrganization.getSize(), updatedOrganization.getWebsiteLink(), String.join(",", updatedOrganization.getTargetInfluencerNiche()), updatedOrganization.getLocation(), updatedOrganization.getBio(), updatedOrganization.getInstagram(), updatedOrganization.getFacebook(), updatedOrganization.getTwitter(), updatedOrganization.getTiktok(), updatedOrganization.getYoutube(), updatedOrganization.getTwitch(), id);

        if(updated == 0) {
        	logger.error("Organization not found with id: {}", id);
            throw new RuntimeException("Failed to update. Organization not found with id: " + id);
        }
        return getById(id);
    }

    public List<Organization> getAll() {
    	logger.info("Attempting to get all organizations");
        String sql = "SELECT * FROM organizations";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM organizations WHERE orgID = ?";
        logger.info("Attempting to delete organization by id {}", id);
        int deleted = jdbcTemplate.update(sql, id);
        if(deleted == 0) {
        	logger.error("Organization not found with id: {}", id);
            throw new RuntimeException("Failed to delete. Organization not found with id: " + id);
        }
    }
}
