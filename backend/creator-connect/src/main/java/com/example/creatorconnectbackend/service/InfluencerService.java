package com.example.creatorconnectbackend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.example.creatorconnectbackend.Interfaces.InfluencerServiceInterface;
import com.example.creatorconnectbackend.model.Gender;
import com.example.creatorconnectbackend.model.Influencer;
import com.example.creatorconnectbackend.model.User;

import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

@Service
public class InfluencerService implements InfluencerServiceInterface {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private UserService userService;

    public InfluencerService(JdbcTemplate jdbcTemplate, UserService userService ) {
        this.jdbcTemplate = jdbcTemplate;
        this.userService = userService;
    }

    private RowMapper<Influencer> rowMapper = (rs, rowNum) -> {
        Influencer influencer = new Influencer();
        influencer.setInfluencerID(rs.getLong("influencerID"));
        influencer.setName(rs.getString("name"));
        influencer.setProfileImage(rs.getString("profileImage"));
        influencer.setGender(Gender.valueOf(rs.getString("gender")));
        influencer.setInfluencerName(rs.getString("influencerName"));
        influencer.setInfluencerType(rs.getString("influencerType"));
        influencer.setInterestedIn(rs.getString("interestedIn"));
        influencer.setMinRate(rs.getLong("minRate"));
        influencer.setPreviousBrands(rs.getString("previousBrands"));
        influencer.setLocation(rs.getString("location"));
        influencer.setBestPosts(rs.getString("bestPosts"));
        return influencer;
    };

    public Influencer register(Influencer influencer, Long userId) {
        // Fetch the user using the provided userId
        User user = jdbcTemplate.queryForObject("SELECT * FROM users WHERE UserID = ?", new Object[]{userId}, userService.getUserRowMapper());

        // Only proceed if the user type is 'Influencer'
        if (user != null && user.getUser_type().equals("Influencer")) {

            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            jdbcInsert.withTableName("influencers");

            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("InfluencerID", userId);
            parameters.put("Name", influencer.getName());
            parameters.put("ProfileImage", influencer.getProfileImage());
            parameters.put("gender", influencer.getGender());
            parameters.put("InfluencerName", influencer.getInfluencerName());
            parameters.put("InfluencerType", influencer.getInfluencerType());
            parameters.put("InterestedIn", influencer.getInterestedIn());
            parameters.put("MinRate", influencer.getMinRate());
            parameters.put("PreviousBrands", influencer.getPreviousBrands());
            parameters.put("Location", influencer.getLocation());
            parameters.put("BestPosts", influencer.getBestPosts());

            jdbcInsert.execute(parameters);

            return influencer;
        } else {
            return null;
        }
    }


    public Influencer getById(Long id) {
        String sql = "SELECT * FROM influencers WHERE influencerID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Influencer not found with id: " + id);
        }
    }

    public Influencer update(Long id, Influencer updatedInfluencer) {
        String sql = "UPDATE influencers SET name = ?, profileImage = ?, gender = ?, influencerName = ?, influencerType = ?, interestedIn = ?, minRate = ?, previousBrands = ?, location = ?, bestPosts = ? WHERE influencerID = ?";
        int updated = jdbcTemplate.update(sql, updatedInfluencer.getName(), updatedInfluencer.getProfileImage(), updatedInfluencer.getGender().name(), updatedInfluencer.getInfluencerName(), updatedInfluencer.getInfluencerType(), updatedInfluencer.getInterestedIn(), updatedInfluencer.getMinRate(), updatedInfluencer.getPreviousBrands(), updatedInfluencer.getLocation(), updatedInfluencer.getBestPosts(), id);
        if(updated == 0) {
            throw new RuntimeException("Failed to update. Influencer not found with id: " + id);
        }
        return getById(id);
    }

    public List<Influencer> getAll() {
        String sql = "SELECT * FROM influencers";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM influencers WHERE influencerID = ?";
        int deleted = jdbcTemplate.update(sql, id);
        if(deleted == 0) {
            throw new RuntimeException("Failed to delete. Influencer not found with id: " + id);
        }
    }
}
