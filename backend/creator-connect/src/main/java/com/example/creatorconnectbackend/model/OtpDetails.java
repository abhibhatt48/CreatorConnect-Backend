package com.example.creatorconnectbackend.model;

public class OtpDetails {
	private String otp;
    private long timestamp;

    public OtpDetails(String otp, long timestamp) {
        this.otp = otp;
        this.timestamp = timestamp;
    }

    public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
