package com.example.creatorconnectbackend.services;

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
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import com.example.creatorconnectbackend.interfaces.InfluencerServiceInterface;
import com.example.creatorconnectbackend.models.Gender;
import com.example.creatorconnectbackend.models.Influencer;
import com.example.creatorconnectbackend.models.User;

@Service
public class InfluencerService implements InfluencerServiceInterface {
    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(InfluencerService.class);

    @Autowired
    private UserService userService;

	private SimpleJdbcInsert jdbcInsert;

    public InfluencerService(JdbcTemplate jdbcTemplate, UserService userService ) {
        this.jdbcTemplate = jdbcTemplate;
        this.userService = userService;
    }
    
    public void setJdbcInsert(SimpleJdbcInsert jdbcInsert) {
        this.jdbcInsert = jdbcInsert;
    }

    private RowMapper<Influencer> rowMapper = (rs, rowNum) -> {
        Influencer influencer = new Influencer();
        influencer.setInfluencerID(rs.getLong("influencerID"));
        influencer.setName(rs.getString("name"));
        influencer.setProfileImage(rs.getString("profileImage"));
        influencer.setGender(Gender.valueOf(rs.getString("gender")));
        influencer.setInfluencerName(rs.getString("influencerName"));
        influencer.setInfluencerType(rs.getString("influencerType"));
        influencer.setInfluencerNiche(Arrays.asList(rs.getString("influencerNiche").split(",")));
        influencer.setMinRate(rs.getLong("minRate"));
        influencer.setPreviousBrands(rs.getString("previousBrands"));
        influencer.setLocation(rs.getString("location"));
        influencer.setBio(rs.getString("bio"));
        influencer.setBirthdate(rs.getDate("birthdate").toLocalDate());
        influencer.setInstagram(rs.getString("instagram"));
        influencer.setTikTok(rs.getString("tikTok"));
        influencer.setTweeter(rs.getString("tweeter"));
        influencer.setYoutube(rs.getString("youtube"));
        influencer.setFacebook(rs.getString("facebook"));
        influencer.setTwitch(rs.getString("twitch"));
        influencer.setBestPosts(Arrays.asList(rs.getString("bestPosts").split(",")));
        return influencer;
    };

    public Influencer register(Influencer influencer, Long userId) {
    	logger.info("Attempting to register influencer with userId {}", userId);
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
            parameters.put("InfluencerNiche", String.join(",", influencer.getInfluencerNiche()));
            parameters.put("MinRate", influencer.getMinRate());
            parameters.put("PreviousBrands", influencer.getPreviousBrands());
            parameters.put("Location", influencer.getLocation());
            parameters.put("Bio", influencer.getBio());
            parameters.put("Birthdate", influencer.getBirthdate());
            parameters.put("Instagram", influencer.getInstagram());
            parameters.put("TikTok", influencer.getTikTok());
            parameters.put("Tweeter", influencer.getTweeter());
            parameters.put("Youtube", influencer.getYoutube());
            parameters.put("Facebook", influencer.getFacebook());
            parameters.put("Twitch", influencer.getTwitch());
            parameters.put("BestPosts", String.join(",", influencer.getBestPosts()));

            jdbcInsert.execute(parameters);

            return influencer;
        } else {
            return null;
        }
    }


    public Influencer getById(Long id) {
        String sql = "SELECT * FROM influencers WHERE influencerID = ?";
        logger.info("Attempting to get influencer by id {}", id);
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
        } catch (EmptyResultDataAccessException e) {
        	logger.error("Influencer not found with id: {}", id, e);
            throw new RuntimeException("Influencer not found with id: " + id);
        }
    }

    public Influencer update(Long id, Influencer updatedInfluencer) {
    	String sql = "UPDATE influencers SET name = ?, profileImage = ?, gender = ?, influencerName = ?, influencerType = ?, influencerNiche = ?, minRate = ?, previousBrands = ?, location = ?, bestPosts = ?, bio = ?, birthdate = ?, instagram = ?, tikTok = ?, tweeter = ?, youtube = ?, facebook = ?, twitch = ? WHERE influencerID = ?";

        logger.info("Attempting to update influencer with id {}", id);
        int updated = jdbcTemplate.update(sql, updatedInfluencer.getName(), updatedInfluencer.getProfileImage(), updatedInfluencer.getGender().name(), updatedInfluencer.getInfluencerName(), updatedInfluencer.getInfluencerType(), String.join(",", updatedInfluencer.getInfluencerNiche()), updatedInfluencer.getMinRate(), updatedInfluencer.getPreviousBrands(), updatedInfluencer.getLocation(), String.join(",", updatedInfluencer.getBestPosts()), updatedInfluencer.getBio(), updatedInfluencer.getBirthdate(), updatedInfluencer.getInstagram(), updatedInfluencer.getTikTok(), updatedInfluencer.getTweeter(), updatedInfluencer.getYoutube(), updatedInfluencer.getFacebook(), updatedInfluencer.getTwitch(), id);

        if(updated == 0) {
        	logger.error("Influencer not found with id: {}", id);
            throw new RuntimeException("Failed to update. Influencer not found with id: " + id);
        }
        return getById(id);
    }

    public List<Influencer> getAll() {
        String sql = "SELECT * FROM influencers";
        logger.info("Attempting to get all influencers");
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM influencers WHERE influencerID = ?";
        logger.info("Attempting to delete influencer by id {}", id);
        int deleted = jdbcTemplate.update(sql, id);
        if(deleted == 0) {
        	logger.error("Influencer not found with id: {}", id);
            throw new RuntimeException("Failed to delete. Influencer not found with id: " + id);
        }
    }
}

