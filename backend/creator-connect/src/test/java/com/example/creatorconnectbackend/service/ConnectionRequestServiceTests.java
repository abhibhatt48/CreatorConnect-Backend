package com.example.creatorconnectbackend.service;


import com.example.creatorconnectbackend.model.ConnectionRequest;
import com.example.creatorconnectbackend.model.Influencer;
import com.example.creatorconnectbackend.model.RequestStatus;
import com.example.creatorconnectbackend.service.ConnectionRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ConnectionRequestServiceTests {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ConnectionRequestService connectionRequestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private static java.sql.ResultSet createMockResultSet() throws java.sql.SQLException {
        java.sql.ResultSet rs = Mockito.mock(java.sql.ResultSet.class);
        when(rs.getLong("RequestID")).thenReturn(1L);
        when(rs.getLong("OrgID")).thenReturn(2L);
        when(rs.getLong("InfluencerID")).thenReturn(3L);
        when(rs.getString("RequestMessage")).thenReturn("Test message");
        when(rs.getString("RequestStatus")).thenReturn("Pending");
        return rs;
    }

    @Test
    void testRowMapper() throws SQLException {
        // Prepare a mock ResultSet with the required values
        RowMapper<ConnectionRequest> rowMapper = connectionRequestService.getRowMapper();
        ConnectionRequest connectionRequest = rowMapper.mapRow(createMockResultSet(), 1);

        // Verify that the ConnectionRequest object is correctly mapped
        assertEquals(1L, connectionRequest.getRequestID());
        assertEquals(2L, connectionRequest.getOrgID());
        assertEquals(3L, connectionRequest.getInfluencerID());
        assertEquals("Test message", connectionRequest.getRequestMessage());
        assertEquals(RequestStatus.Pending, connectionRequest.getRequestStatus());
    }

    @Test
    void testCreateRequest_NotNullConnectionRequest_ReturnsCreatedRequest() {
        // Prepare a mock ConnectionRequest object
        ConnectionRequest connectionRequest = Mockito.mock(ConnectionRequest.class);
        ConnectionRequestService connectionRequestService = Mockito.mock(ConnectionRequestService.class);
        when(connectionRequest.getOrgID()).thenReturn(1L);
        when(connectionRequest.getInfluencerID()).thenReturn(2L);
        when(connectionRequest.getRequestMessage()).thenReturn("Test message");
        when(connectionRequest.getRequestStatus()).thenReturn(RequestStatus.valueOf(String.valueOf(RequestStatus.Pending)));

        // Invoke the createRequest method
        Mockito.doReturn(connectionRequest).when(connectionRequestService).createRequest(Mockito.any(ConnectionRequest.class));
        ConnectionRequest result = connectionRequestService.createRequest(connectionRequest);

        // Verify that the SimpleJdbcInsert and MapSqlParameterSource were used correctly
        assertEquals(connectionRequest, result);
    }

    @Test
    void testCreateRequest_NullConnectionRequest_ReturnsNull() {
        // Mock the JdbcTemplate
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);

        // Create an instance of ConnectionRequestService with the mocked JdbcTemplate
        ConnectionRequestService connectionRequestService = new ConnectionRequestService(jdbcTemplate);

        // Invoke the createRequest method with a null connectionRequest
        ConnectionRequest result = connectionRequestService.createRequest(null);

        // Verify that null is returned
        assertNull(result);
    }

    @Test
    void testCreateRequest_NullRequest_ReturnsNull() {
        ConnectionRequest result = connectionRequestService.createRequest(null);

        assertNull(result);
        verify(jdbcTemplate, never()).update(anyString(), any(Object[].class));
        verify(jdbcTemplate, never()).queryForObject(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    void testGetConnectionRequestByID_ValidID_ReturnsConnectionRequest() {
        Long id = 1L;

        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.setRequestID(id);
        connectionRequest.setOrgID(1L);
        connectionRequest.setInfluencerID(1L);
        connectionRequest.setRequestMessage("Test message");
        connectionRequest.setRequestStatus(RequestStatus.Pending);

        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(connectionRequest);

        ConnectionRequest result = connectionRequestService.getConnectionRequestByID(id);

        assertNotNull(result);
        assertEquals(connectionRequest, result);
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    void testGetConnectionRequestByID_InvalidID_ThrowsException() {
        Long id = 1L;

        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class))).thenThrow(EmptyResultDataAccessException.class);

        assertThrows(RuntimeException.class, () -> connectionRequestService.getConnectionRequestByID(id));

        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    void testUpdateStatus_ValidID_ReturnsUpdatedConnectionRequest() {
        Long id = 1L;
        RequestStatus newStatus = RequestStatus.Accepted;

        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.setRequestID(id);
        connectionRequest.setOrgID(1L);
        connectionRequest.setInfluencerID(1L);
        connectionRequest.setRequestMessage("Test message");
        connectionRequest.setRequestStatus(newStatus);

        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);
        when(connectionRequestService.getConnectionRequestByID(id)).thenReturn(connectionRequest);

        ConnectionRequest result = connectionRequestService.updateStatus(id, newStatus);

        assertNotNull(result);
        assertEquals(connectionRequest, result);
        verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));
//        verify(connectionRequestService, times(1)).getConnectionRequestByID(id);
    }

    @Test
    void testUpdateStatus_InvalidID_ThrowsException() {
        Long id = 1L;
        RequestStatus newStatus = RequestStatus.Accepted;

        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(0);

        assertThrows(RuntimeException.class, () -> connectionRequestService.updateStatus(id, newStatus));

        verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));
    }

    @Test
    void testGetRequestsByInfluencerID_ValidID_ReturnsListOfConnectionRequests() {
        Long influencerID = 1L;

        List<ConnectionRequest> connectionRequests = new ArrayList<>();
        connectionRequests.add(new ConnectionRequest());
        connectionRequests.add(new ConnectionRequest());

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(connectionRequests);

        List<ConnectionRequest> result = connectionRequestService.getRequestsByInfluencerID(influencerID);

        assertNotNull(result);
        assertEquals(connectionRequests.size(), result.size());
        assertEquals(connectionRequests, result);
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    void testGetRequestsByInfluencerID_InvalidID_ReturnsEmptyList() {
        Long influencerID = 1L;

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenThrow(EmptyResultDataAccessException.class);

        List<ConnectionRequest> result = connectionRequestService.getRequestsByInfluencerID(influencerID);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    void testGetRequestsByOrgID_ValidID_ReturnsListOfConnectionRequests() {
        Long orgID = 1L;

        List<ConnectionRequest> connectionRequests = new ArrayList<>();
        connectionRequests.add(new ConnectionRequest());
        connectionRequests.add(new ConnectionRequest());

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(connectionRequests);

        List<ConnectionRequest> result = connectionRequestService.getRequestsByOrgID(orgID);

        assertNotNull(result);
        assertEquals(connectionRequests.size(), result.size());
        assertEquals(connectionRequests, result);
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    void testGetRequestsByOrgID_InvalidID_ReturnsEmptyList() {
        Long orgID = 1L;

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenThrow(EmptyResultDataAccessException.class);

        List<ConnectionRequest> result = connectionRequestService.getRequestsByOrgID(orgID);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    void testGetRequestsByStatus_ValidParameters_ReturnsListOfConnectionRequests() {
        Long orgID = 1L;
        String status = "PENDING";

        List<ConnectionRequest> connectionRequests = new ArrayList<>();
        connectionRequests.add(new ConnectionRequest());
        connectionRequests.add(new ConnectionRequest());

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(connectionRequests);

        List<ConnectionRequest> result = connectionRequestService.getRequestsByStatus(orgID, status);

        assertNotNull(result);
        assertEquals(connectionRequests.size(), result.size());
        assertEquals(connectionRequests, result);
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    void testGetRequestsByStatus_InvalidParameters_ReturnsEmptyList() {
        Long orgID = 1L;
        String status = "PENDING";

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenThrow(EmptyResultDataAccessException.class);

        List<ConnectionRequest> result = connectionRequestService.getRequestsByStatus(orgID, status);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    void testGetAllRequests_ReturnsListOfConnectionRequests() {
        List<ConnectionRequest> connectionRequests = new ArrayList<>();
        connectionRequests.add(new ConnectionRequest());
        connectionRequests.add(new ConnectionRequest());

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(connectionRequests);

        List<ConnectionRequest> result = connectionRequestService.getAllRequests();

        assertNotNull(result);
        assertEquals(connectionRequests.size(), result.size());
        assertEquals(connectionRequests, result);
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    void testGetAllRequests_ReturnsEmptyList() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenThrow(EmptyResultDataAccessException.class);

        List<ConnectionRequest> result = connectionRequestService.getAllRequests();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    void testDeleteByID_ValidID_DeletesConnectionRequest() {
        Long id = 1L;

        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        assertDoesNotThrow(() -> connectionRequestService.deleteByID(id));

        verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));
    }

    @Test
    void testDeleteByID_InvalidID_ThrowsException() {
        Long id = 1L;

        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(0);

        assertThrows(RuntimeException.class, () -> connectionRequestService.deleteByID(id));

        verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));
    }

    @Test
    void testUpdateMessage_ValidID_ReturnsUpdatedConnectionRequest() {
        Long id = 1L;
        Map<String, String> map = new HashMap<>();
        map.put("Message", "Hello!");

        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.setRequestID(id);
        connectionRequest.setOrgID(1L);
        connectionRequest.setInfluencerID(1L);
        connectionRequest.setRequestMessage(map.get("Message"));
        connectionRequest.setRequestStatus(RequestStatus.Pending);

        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);
        when(connectionRequestService.getConnectionRequestByID(id)).thenReturn(connectionRequest);

        ConnectionRequest result = connectionRequestService.updateMessage(id, map);

        assertNotNull(result);
        assertEquals(connectionRequest, result);
        verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));
    }

    @Test
    void testUpdateMessage_InvalidID_ThrowsException() {
        Long id = 1L;
        Map<String, String> map = new HashMap<>();
        map.put("Message", "Hello!");
        //ConnectionRequestService connectionRequestService = Mockito.mock(ConnectionRequestService.class);
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(0);

        assertThrows(RuntimeException.class, () -> connectionRequestService.updateMessage(id, map));

        verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));
    }

}
