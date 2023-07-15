package com.example.creatorconnectbackend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import com.example.creatorconnectbackend.Interfaces.OrganizationServiceInterface;
import com.example.creatorconnectbackend.model.Organization;
import com.example.creatorconnectbackend.model.User;

@Service
public class OrganizationService implements OrganizationServiceInterface {
    private final JdbcTemplate jdbcTemplate;
    
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
        organization.setTargetInfluencerType(rs.getString("targetInfluencerType"));
        organization.setLocation(rs.getString("location"));
        return organization;
    };

    public Organization register(Organization organization, Long userId) {
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
            parameters.put("targetInfluencerType", organization.getTargetInfluencerType());
            parameters.put("location", organization.getLocation());
            
            jdbcInsert.execute(parameters);
            
            return organization;
        } else {
        	return null;
        }
    }

    
    public Organization getById(Long id) {
        String sql = "SELECT * FROM organizations WHERE orgID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Organization not found with id: " + id);
        }
    }

    public Organization update(Long id, Organization updatedOrganization) {
        String sql = "UPDATE organizations SET orgName = ?, profileImage = ?, companyType = ?, size = ?, websiteLink = ?, targetInfluencerType = ?, location = ? WHERE orgID = ?";
        int updated = jdbcTemplate.update(sql, updatedOrganization.getOrgName(), updatedOrganization.getProfileImage(), updatedOrganization.getCompanyType(), updatedOrganization.getSize(), updatedOrganization.getWebsiteLink(), updatedOrganization.getTargetInfluencerType(), updatedOrganization.getLocation(), id);
        if(updated == 0) {
            throw new RuntimeException("Failed to update. Organization not found with id: " + id);
        }
        return getById(id);
    }

    public List<Organization> getAll() {
        String sql = "SELECT * FROM organizations";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM organizations WHERE orgID = ?";
        int deleted = jdbcTemplate.update(sql, id);
        if(deleted == 0) {
            throw new RuntimeException("Failed to delete. Organization not found with id: " + id);
        }
    }
}
