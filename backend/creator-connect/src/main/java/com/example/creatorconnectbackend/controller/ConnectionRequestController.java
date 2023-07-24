package com.example.creatorconnectbackend.controllers;

import com.example.creatorconnectbackend.models.ConnectionRequest;
import com.example.creatorconnectbackend.models.RequestStatus;
import com.example.creatorconnectbackend.services.ConnectionRequestService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/connectionReq")
public class ConnectionRequestController {

    private final ConnectionRequestService connectionRequestService;
    private final Logger logger = LoggerFactory.getLogger(ConnectionRequestController.class);

    @Autowired
    public ConnectionRequestController(ConnectionRequestService connectionRequestService) {
        this.connectionRequestService = connectionRequestService;
    }

    @PostMapping("/create")
    public ResponseEntity<ConnectionRequest> createRequest(@Valid @RequestBody ConnectionRequest connectionRequest) {
    	logger.info("Creating new connection request");
        ConnectionRequest createdRequest = connectionRequestService.createRequest(connectionRequest);
        logger.info("Created connection request with ID: {}", createdRequest.getRequestID());
        return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
    }

    @GetMapping("/getByRequestID/{requestId}")
    public ResponseEntity<ConnectionRequest> getConnectionById(@PathVariable("requestId") Long requestId) {
    	logger.info("Getting connection request with ID: {}", requestId);
        ConnectionRequest connectionRequest = connectionRequestService.getConnectionRequestByID(requestId);
        return ResponseEntity.ok(connectionRequest);
    }

    @PutMapping("/update/{requestId}")
    public ResponseEntity<String> updateConnectionRequestStatus(
            @PathVariable Long requestId,
            @RequestBody Map<String, String> payload) {

        String requestStatus = payload.get("requestStatus");

        if (requestStatus != null) {
        	logger.info("Updating connection request with ID: {}", requestId);
            connectionRequestService.updateStatus(requestId, RequestStatus.valueOf(requestStatus));
            logger.info("Updated connection request with ID: {}", requestId);
            return ResponseEntity.ok("Connection request updated successfully");
        } else {
        	logger.warn("Invalid request payload for updating connection request");
            return ResponseEntity.badRequest().body("Invalid request payload");
        }
    }

    @GetMapping("/influencer/getByID/{id}")
    public ResponseEntity<List<ConnectionRequest>> getRequestsByInfluencerID(@PathVariable("id") Long influencerID) {
        List<ConnectionRequest> requests = connectionRequestService.getRequestsByInfluencerID(influencerID);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/organization/getByID/{id}")
    public ResponseEntity<List<ConnectionRequest>> getRequestsByOrganizationID(@PathVariable("id") Long orgID) {
        List<ConnectionRequest> requests = connectionRequestService.getRequestsByOrgID(orgID);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/organization/{orgID}/status/{status}")
    public ResponseEntity<List<ConnectionRequest>> getRequestsByStatus(@PathVariable("orgID") Long orgID, @PathVariable("status") String status) {
        List<ConnectionRequest> requests = connectionRequestService.getRequestsByStatus(orgID, status);
        return ResponseEntity.ok(requests);
    }


    @GetMapping("/getAll")
    public List<ConnectionRequest> getAllRequests() {
        List<ConnectionRequest> allRequests = connectionRequestService.getAllRequests();
        return ResponseEntity.ok(allRequests).getBody();
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteByID(@PathVariable("id") Long id) {
    	logger.info("Deleting connection request with ID: {}", id);
        connectionRequestService.deleteByID(id);
        logger.info("Deleted connection request with ID: {}",    id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/updateMessage/{id}")
    public ResponseEntity<ConnectionRequest> updateRequestMessage(@PathVariable("id") Long requestId, @RequestBody Map<String, String> map) {
    	logger.info("Updating connection request message with ID: {}", requestId);
        ConnectionRequest updatedRequest = connectionRequestService.updateMessage(requestId, map);
        logger.info("Updated connection request message with ID: {}", requestId);
        return ResponseEntity.ok(updatedRequest);
    }

}