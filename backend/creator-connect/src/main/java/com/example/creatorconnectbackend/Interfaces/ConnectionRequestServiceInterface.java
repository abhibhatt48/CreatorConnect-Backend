package com.example.creatorconnectbackend.Interfaces;

import com.example.creatorconnectbackend.model.ConnectionRequest;
import com.example.creatorconnectbackend.model.RequestStatus;

import java.util.List;

public interface ConnectionRequestServiceInterface {

    ConnectionRequest createRequest(ConnectionRequest connectionRequest);
    ConnectionRequest getConnectionRequestByID(Long requestID);
    ConnectionRequest updateStatus(Long id, RequestStatus newStatus);
    List<ConnectionRequest> getRequestsByInfluencerID(Long id);
    List<ConnectionRequest> getAllRequests();
    void deleteByID(Long id);
    ConnectionRequest updateMessage(Long id, String message);
}
