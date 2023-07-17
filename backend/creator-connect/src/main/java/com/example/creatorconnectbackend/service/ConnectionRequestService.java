package com.example.creatorconnectbackend.service;

import com.example.creatorconnectbackend.Interfaces.ConnectionRequestServiceInterface;
import com.example.creatorconnectbackend.model.ConnectionRequest;
import com.example.creatorconnectbackend.model.RequestStatus;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConnectionRequestService implements ConnectionRequestServiceInterface {

    private final JdbcTemplate jdbcTemplate;

    public ConnectionRequestService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<ConnectionRequest> rowMapper = (rs, rowNum) -> {
        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.setRequestID(rs.getLong("RequestID"));
        connectionRequest.setOrgID(rs.getLong("OrgID"));
        connectionRequest.setInfluencerID(rs.getLong("InfluencerID"));
        connectionRequest.setRequestMessage(rs.getString("RequestMessage"));
        connectionRequest.setRequestStatus(RequestStatus.valueOf(rs.getString("RequestStatus")));
        return connectionRequest;
    };

    public RowMapper<ConnectionRequest> getRowMapper() {
        return rowMapper;
    }

    public ConnectionRequest createRequest(ConnectionRequest connectionRequest) {
        if (connectionRequest != null) {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            jdbcInsert.withTableName("connection_requests").usingGeneratedKeyColumns("RequestID");

            Map<String, Object> params = new HashMap<>();
            params.put("OrgID", connectionRequest.getOrgID());
            params.put("InfluencerID", connectionRequest.getInfluencerID());
            params.put("RequestMessage", connectionRequest.getRequestMessage());
            params.put("RequestStatus", connectionRequest.getRequestStatus().name());

            Number generatedId = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
            connectionRequest.setRequestID(generatedId.longValue());

            return connectionRequest;
        } else {
            return null;
        }
    }

    public ConnectionRequest getConnectionRequestByID(Long requestID) {
        String query = "SELECT * FROM connection_requests WHERE RequestID = ?";
        try {
            return jdbcTemplate.queryForObject(query, new Object[]{requestID}, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Connection request not found with ID: " + requestID);
        }
    }


    public ConnectionRequest updateStatus(Long id, RequestStatus newStatus) {
        String query = "UPDATE connection_requests SET RequestStatus = ? WHERE RequestID = ?";
        int updated = jdbcTemplate.update(query, newStatus.name(), id);
        if (updated == 0) {
            throw new RuntimeException("Could not update the status of requestID: " + id);
        }
        return getConnectionRequestByID(id);
    }


    public List<ConnectionRequest> getRequestsByInfluencerID(Long id) {
        String query = "SELECT * FROM connection_requests WHERE InfluencerID = ?";
        try {
            return jdbcTemplate.query(query, new Object[]{id}, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<ConnectionRequest> getRequestsByOrgID(Long orgID) {
        String query = "SELECT * FROM connection_requests WHERE OrgID = ?";
        try {
            return jdbcTemplate.query(query, new Object[]{orgID}, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<ConnectionRequest> getRequestsByStatus(Long orgID, String status) {
        String query = "SELECT * FROM connection_requests WHERE OrgID = ? AND RequestStatus = ?";
        try {
            return jdbcTemplate.query(query, new Object[]{orgID, status}, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }


    public List<ConnectionRequest> getAllRequests() {
        String query = "SELECT * FROM connection_requests";
        try {
            return jdbcTemplate.query(query, new Object[]{}, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public void deleteByID(Long id) {
        String query = "DELETE FROM connection_requests WHERE RequestID = ?";
        int deletedRows = jdbcTemplate.update(query, id);
        if (deletedRows == 0) {
            throw new RuntimeException("Failed to delete connection request with ID: " + id);
        }
    }


    public ConnectionRequest updateMessage(Long id, Map<String, String> map) {
        String query = "UPDATE connection_requests SET RequestMessage = ? WHERE RequestID = ?";
        String message = map.get("Message");
        int updatedRows = jdbcTemplate.update(query, message, id);
        if (updatedRows == 0) {
            throw new RuntimeException("Failed to update message for connection request with ID: " + id);
        }
        return getConnectionRequestByID(id);
    }


}
