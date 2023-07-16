package com.example.creatorconnectbackend.model;

public class ConnectionRequest {

	private Long requestID;
    private Long orgID;
    private Long influencerID;
    private String requestMessage;
    private RequestStatus requestStatus;
	public Long getRequestID() {
		return requestID;
	}
	public void setRequestID(Long requestID) {
		this.requestID = requestID;
	}
	public Long getOrgID() {
		return orgID;
	}
	public void setOrgID(Long orgID) {
		this.orgID = orgID;
	}
	public Long getInfluencerID() {
		return influencerID;
	}
	public void setInfluencerID(Long influencerID) {
		this.influencerID = influencerID;
	}
	public String getRequestMessage() {
		return requestMessage;
	}
	public void setRequestMessage(String requestMessage) {
		this.requestMessage = requestMessage;
	}
	public RequestStatus getRequestStatus() {
		return requestStatus;
	}
	public void setRequestStatus(RequestStatus requestStatus) {
		this.requestStatus = requestStatus;
	}    
}

