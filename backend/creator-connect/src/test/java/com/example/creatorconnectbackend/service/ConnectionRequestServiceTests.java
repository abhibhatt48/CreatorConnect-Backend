package com.example.creatorconnectbackend.service;

import com.example.creatorconnectbackend.Interfaces.ConnectionRequestServiceInterface;
import com.example.creatorconnectbackend.model.ConnectionRequest;
import com.example.creatorconnectbackend.model.RequestStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

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

//    @Test
//    void testCreateRequest_ValidInput_ReturnsConnectionRequest() {
//        // Create the connection request object
//        ConnectionRequest connectionRequest = new ConnectionRequest();
//        connectionRequest.setOrgID(1L);
//        connectionRequest.setInfluencerID(2L);
//        connectionRequest.setRequestMessage("Request message");
//        connectionRequest.setRequestStatus(RequestStatus.Pending);
//
//        // Mock the behavior of SimpleJdbcInsert
//        SimpleJdbcInsert jdbcInsert = mock(SimpleJdbcInsert.class);
//        when(jdbcInsert.withTableName(anyString())).thenReturn(jdbcInsert);
//        when(jdbcInsert.usingGeneratedKeyColumns(anyString())).thenReturn(jdbcInsert);
//        when(jdbcInsert.executeAndReturnKey(any(MapSqlParameterSource.class))).thenReturn(1L);
//
//        // Mock the behavior of jdbcTemplate.update
//        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);
//
//        // Set the mocked SimpleJdbcInsert to the ConnectionRequestService
//        connectionRequestService.setSimpleJdbcInsert(jdbcInsert);
//
//        // Call the method under test
//        ConnectionRequest result = connectionRequestService.createRequest(connectionRequest);
//
//        // Verify the result
//        assertNotNull(result);
//        assertEquals(connectionRequest, result);
//
//        // Verify the SimpleJdbcInsert behavior
//        verify(jdbcInsert, times(1)).withTableName(anyString());
//        verify(jdbcInsert, times(1)).usingGeneratedKeyColumns(anyString());
//        verify(jdbcInsert, times(1)).executeAndReturnKey(any(MapSqlParameterSource.class));
//
//        // Verify the jdbcTemplate.update behavior
//        verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));
//    }
//
//    @Test
//    void testCreateRequest_NullInput_ReturnsNull() {
//        // Call the method under test with null input
//        ConnectionRequest result = connectionRequestService.createRequest(null);
//
//        // Verify the result is null
//        assertNull(result);
//
//        // Verify that SimpleJdbcInsert and jdbcTemplate.update were not called
//        verifyNoInteractions(connectionRequestService.getSimpleJdbcInsert(), jdbcTemplate);
//    }

    @Test
    void testGetConnectionRequestByID_ValidID_ReturnsConnectionRequest() {
        Long requestId = 1L;

        // Create the expected connection request
        ConnectionRequest expectedRequest = new ConnectionRequest();
        expectedRequest.setRequestID(requestId);
        // Set other properties of the expected connection request

        // Mock the behavior of jdbcTemplate.queryForObject
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(expectedRequest);

        // Call the method under test
        ConnectionRequest result = connectionRequestService.getConnectionRequestByID(requestId);

        // Verify the result
        assertNotNull(result);
        assertEquals(expectedRequest, result);

        // Verify the jdbcTemplate.queryForObject behavior
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    void testGetConnectionRequestByID_InvalidID_ThrowsException() {
        Long requestId = 1L;

        // Mock the behavior of jdbcTemplate.queryForObject to throw EmptyResultDataAccessException
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class))).thenThrow(EmptyResultDataAccessException.class);

        // Call the method under test and verify that it throws an exception
        assertThrows(RuntimeException.class, () -> connectionRequestService.getConnectionRequestByID(requestId));

        // Verify the jdbcTemplate.queryForObject behavior
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    void testUpdateStatus_ValidID_ReturnsUpdatedConnectionRequest() {
        Long requestId = 1L;
        RequestStatus newStatus = RequestStatus.Accepted;

        // Mock the behavior of jdbcTemplate.update
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        // Mock the behavior of getConnectionRequestByID
        ConnectionRequest expectedRequest = new ConnectionRequest();
        expectedRequest.setRequestID(requestId);
        // Set other properties of the expected connection request
        when(connectionRequestService.getConnectionRequestByID(requestId)).thenReturn(expectedRequest);

        // Call the method under test
        ConnectionRequest result = connectionRequestService.updateStatus(requestId, newStatus);

        // Verify the result
        assertNotNull(result);
        assertEquals(expectedRequest, result);

        // Verify the jdbcTemplate.update behavior
        verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));

        // Verify the getConnectionRequestByID behavior
        verify(connectionRequestService, times(1)).getConnectionRequestByID(requestId);
    }

    @Test
    void testUpdateStatus_InvalidID_ThrowsException() {
        Long requestId = 1L;
        RequestStatus newStatus = RequestStatus.Accepted;

        // Mock the behavior of jdbcTemplate.update to return 0 (no rows updated)
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(0);

        // Call the method under test and verify that it throws an exception
        assertThrows(RuntimeException.class, () -> connectionRequestService.updateStatus(requestId, newStatus));

        // Verify the jdbcTemplate.update behavior
        verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));

        // Verify that getConnectionRequestByID was not called
        verify(connectionRequestService, never()).getConnectionRequestByID(anyLong());
    }

    @Test
    void testGetRequestsByInfluencerID_ValidID_ReturnsListOfConnectionRequests() {
        Long influencerId = 1L;

        // Create a list of expected connection requests
        List<ConnectionRequest> expectedRequests = new ArrayList<>();
        ConnectionRequest request1 = new ConnectionRequest();
        request1.setRequestID(1L);
        // Set other properties of request1
        ConnectionRequest request2 = new ConnectionRequest();
        request2.setRequestID(2L);
        // Set other properties of request2
        expectedRequests.add(request1);
        expectedRequests.add(request2);

        // Mock the behavior of jdbcTemplate.query
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(expectedRequests);

        // Call the method under test
        List<ConnectionRequest> result = connectionRequestService.getRequestsByInfluencerID(influencerId);

        // Verify the result
        assertNotNull(result);
        assertEquals(expectedRequests.size(), result.size());
        assertEquals(expectedRequests, result);

        // Verify the jdbcTemplate.query behavior
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    void testGetRequestsByInfluencerID_InvalidID_ReturnsEmptyList() {
        Long influencerId = 1L;

        // Mock the behavior of jdbcTemplate.query to throw EmptyResultDataAccessException
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenThrow(EmptyResultDataAccessException.class);

        // Call the method under test
        List<ConnectionRequest> result = connectionRequestService.getRequestsByInfluencerID(influencerId);

        // Verify the result is an empty list
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify the jdbcTemplate.query behavior
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    void testGetAllRequests_ReturnsListOfConnectionRequests() {
        // Create a list of expected connection requests
        List<ConnectionRequest> expectedRequests = new ArrayList<>();
        ConnectionRequest request1 = new ConnectionRequest();
        request1.setRequestID(1L);
        // Set other properties of request1
        ConnectionRequest request2 = new ConnectionRequest();
        request2.setRequestID(2L);
        // Set other properties of request2
        expectedRequests.add(request1);
        expectedRequests.add(request2);

        // Mock the behavior of jdbcTemplate.query
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(expectedRequests);

        // Call the method under test
        List<ConnectionRequest> result = connectionRequestService.getAllRequests();

        // Verify the result
        assertNotNull(result);
        assertEquals(expectedRequests.size(), result.size());
        assertEquals(expectedRequests, result);

        // Verify the jdbcTemplate.query behavior
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    void testGetAllRequests_ReturnsEmptyList() {
        // Mock the behavior of jdbcTemplate.query to throw EmptyResultDataAccessException
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenThrow(EmptyResultDataAccessException.class);

        // Call the method under test
        List<ConnectionRequest> result = connectionRequestService.getAllRequests();

        // Verify the result is an empty list
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify the jdbcTemplate.query behavior
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    void testDeleteByID_ValidID_DeletesConnectionRequest() {
        Long requestId = 1L;

        // Mock the behavior of jdbcTemplate.update
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        // Call the method under test
        assertDoesNotThrow(() -> connectionRequestService.deleteByID(requestId));

        // Verify the jdbcTemplate.update behavior
        verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));
    }

    @Test
    void testDeleteByID_InvalidID_ThrowsException() {
        Long requestId = 1L;

        // Mock the behavior of jdbcTemplate.update to return 0 (no rows deleted)
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(0);

        // Call the method under test and verify that it throws an exception
        assertThrows(RuntimeException.class, () -> connectionRequestService.deleteByID(requestId));

        // Verify the jdbcTemplate.update behavior
        verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));
    }

    @Test
    void testUpdateMessage_ValidID_ReturnsUpdatedConnectionRequest() {
        Long requestId = 1L;
        String newMessage = "New message";

        // Mock the behavior of jdbcTemplate.update
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        // Mock the behavior of getConnectionRequestByID
        ConnectionRequest expectedRequest = new ConnectionRequest();
        expectedRequest.setRequestID(requestId);
        // Set other properties of the expected connection request
        when(connectionRequestService.getConnectionRequestByID(requestId)).thenReturn(expectedRequest);

        // Call the method under test
        ConnectionRequest result = connectionRequestService.updateMessage(requestId, newMessage);

        // Verify the result
        assertNotNull(result);
        assertEquals(expectedRequest, result);

        // Verify the jdbcTemplate.update behavior
        verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));

        // Verify the getConnectionRequestByID behavior
        verify(connectionRequestService, times(1)).getConnectionRequestByID(requestId);
    }

    @Test
    void testUpdateMessage_InvalidID_ThrowsException() {
        Long requestId = 1L;
        String newMessage = "New message";

        // Mock the behavior of jdbcTemplate.update to return 0 (no rows updated)
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(0);

        // Call the method under test and verify that it throws an exception
        assertThrows(RuntimeException.class, () -> connectionRequestService.updateMessage(requestId, newMessage));

        // Verify the jdbcTemplate.update behavior
        verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));

        // Verify that getConnectionRequestByID was not called
        verify(connectionRequestService, never()).getConnectionRequestByID(anyLong());
    }
}

