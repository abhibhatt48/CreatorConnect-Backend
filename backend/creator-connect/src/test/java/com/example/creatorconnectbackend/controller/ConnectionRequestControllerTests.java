package com.example.creatorconnectbackend.controller;

import com.example.creatorconnectbackend.controller.ConnectionRequestController;
import com.example.creatorconnectbackend.model.ConnectionRequest;
import com.example.creatorconnectbackend.model.RequestStatus;
import com.example.creatorconnectbackend.service.ConnectionRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ConnectionRequestControllerTests {

    @Mock
    private ConnectionRequestService connectionRequestService;

    private ConnectionRequestController connectionRequestController;

    @BeforeEach
    void setup() {
        connectionRequestService = mock(ConnectionRequestService.class);
        connectionRequestController = new ConnectionRequestController(connectionRequestService);
    }

    @Test
    void testCreateRequest_ValidConnectionRequest_ReturnsCreatedRequest() {
        // Prepare a mock ConnectionRequest object
        ConnectionRequest connectionRequest = mock(ConnectionRequest.class);

        // Prepare the expected created request
        ConnectionRequest createdRequest = mock(ConnectionRequest.class);

        // Configure the mock service to return the created request
        when(connectionRequestService.createRequest(connectionRequest)).thenReturn(createdRequest);

        // Invoke the createRequest method
        ResponseEntity<ConnectionRequest> response = connectionRequestController.createRequest(connectionRequest);

        // Verify that the service method was called
        verify(connectionRequestService).createRequest(connectionRequest);

        // Verify the response status code and body
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdRequest, response.getBody());
    }

    @Test
    void testGetConnectionById_ValidRequestId_ReturnsConnectionRequest() {
        // Prepare the expected connection request
        ConnectionRequest connectionRequest = mock(ConnectionRequest.class);

        // Configure the mock service to return the connection request
        when(connectionRequestService.getConnectionRequestByID(1L)).thenReturn(connectionRequest);

        // Invoke the getConnectionById method
        ResponseEntity<ConnectionRequest> response = connectionRequestController.getConnectionById(1L);

        // Verify that the service method was called
        verify(connectionRequestService).getConnectionRequestByID(1L);

        // Verify the response status code and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(connectionRequest, response.getBody());
    }

    @Test
    void testUpdateConnectionRequestStatus_ValidRequestIdAndPayload_ReturnsSuccessMessage() {
        // Prepare the request ID and payload
        Long requestId = 1L;
        Map<String, String> payload = new HashMap<>();
        payload.put("requestStatus", "Accepted");

        // Invoke the updateConnectionRequestStatus method
        ResponseEntity<String> response = connectionRequestController.updateConnectionRequestStatus(requestId, payload);

        // Verify that the service method was called
        verify(connectionRequestService).updateStatus(requestId, RequestStatus.Accepted);

        // Verify the response status code and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Connection request updated successfully", response.getBody());
    }

    @Test
    void testUpdateConnectionRequestStatus_InvalidPayload_ReturnsBadRequest() {
        // Prepare the request ID and an invalid payload
        Long requestId = 1L;
        Map<String, String> payload = new HashMap<>();

        // Invoke the updateConnectionRequestStatus method
        ResponseEntity<String> response = connectionRequestController.updateConnectionRequestStatus(requestId, payload);

        // Verify that the service method was not called
        verify(connectionRequestService, never()).updateStatus(anyLong(), any(RequestStatus.class));

        // Verify the response status code and body
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid request payload", response.getBody());
    }

    @Test
    void testGetRequestsByInfluencerID_ValidInfluencerId_ReturnsConnectionRequests() {
        // Prepare the expected connection requests
        List<ConnectionRequest> requests = Arrays.asList(mock(ConnectionRequest.class), mock(ConnectionRequest.class));

        // Configure the mock service to return the connection requests
        when(connectionRequestService.getRequestsByInfluencerID(1L)).thenReturn(requests);

        // Invoke the getRequestsByInfluencerID method
        ResponseEntity<List<ConnectionRequest>> response = connectionRequestController.getRequestsByInfluencerID(1L);

        // Verify that the service method was called
        verify(connectionRequestService).getRequestsByInfluencerID(1L);

        // Verify the response status code and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(requests, response.getBody());
    }

    @Test
    void testGetRequestsByOrganizationID_ValidOrganizationId_ReturnsConnectionRequests() {
        // Prepare the expected connection requests
        List<ConnectionRequest> requests = Arrays.asList(mock(ConnectionRequest.class), mock(ConnectionRequest.class));

        // Configure the mock service to return the connection requests
        when(connectionRequestService.getRequestsByOrgID(1L)).thenReturn(requests);

        // Invoke the getRequestsByOrganizationID method
        ResponseEntity<List<ConnectionRequest>> response = connectionRequestController.getRequestsByOrganizationID(1L);

        // Verify that the service method was called
        verify(connectionRequestService).getRequestsByOrgID(1L);

        // Verify the response status code and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(requests, response.getBody());
    }

    @Test
    void testGetRequestsByStatus_ValidOrganizationIdAndStatus_ReturnsConnectionRequests() {
        // Prepare the expected connection requests
        List<ConnectionRequest> requests = Arrays.asList(mock(ConnectionRequest.class), mock(ConnectionRequest.class));

        // Configure the mock service to return the connection requests
        when(connectionRequestService.getRequestsByStatus(1L, "Pending")).thenReturn(requests);

        // Invoke the getRequestsByStatus method
        ResponseEntity<List<ConnectionRequest>> response = connectionRequestController.getRequestsByStatus(1L, "Pending");

        // Verify that the service method was called
        verify(connectionRequestService).getRequestsByStatus(1L, "Pending");

        // Verify the response status code and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(requests, response.getBody());
    }

    @Test
    void testGetAllRequests_ValidRequest_ReturnsAllConnectionRequests() {
        // Prepare the expected connection requests
        List<ConnectionRequest> requests = Arrays.asList(mock(ConnectionRequest.class), mock(ConnectionRequest.class));

        // Configure the mock service to return the connection requests
        when(connectionRequestService.getAllRequests()).thenReturn(requests);

        // Invoke the getAllRequests method
        List<ConnectionRequest> response = connectionRequestController.getAllRequests();

        // Verify that the service method was called
        verify(connectionRequestService).getAllRequests();

        // Verify the response body
        assertEquals(requests, response);
    }

    @Test
    void testDeleteByID_ValidRequestId_ReturnsNoContent() {
        // Invoke the deleteByID method
        ResponseEntity<String> response = connectionRequestController.deleteByID(1L);

        // Verify that the service method was called
        verify(connectionRequestService).deleteByID(1L);

        // Verify the response status code and body
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void testUpdateRequestMessage_ValidRequestIdAndMessage_ReturnsUpdatedConnectionRequest() {
        // Prepare the expected updated request
        ConnectionRequest updatedRequest = mock(ConnectionRequest.class);

        // Configure the mock service to return the updated request
        Map<String, String> map = new HashMap<>();
        map.put("Message", "Hello!");
        when(connectionRequestService.updateMessage(1L, map)).thenReturn(updatedRequest);

        // Invoke the updateRequestMessage method
        ResponseEntity<ConnectionRequest> response = connectionRequestController.updateRequestMessage(1L, map);

        // Verify that the service method was called
        verify(connectionRequestService).updateMessage(1L, map);

        // Verify the response status code and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedRequest, response.getBody());
    }
}

