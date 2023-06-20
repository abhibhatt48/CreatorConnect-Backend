package com.example.creatorconnectbackend.model;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "connection_requests")
public class ConnectionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long RequestID;

    private Long OrgID;

    private Long InfluencerID;

    @Column(columnDefinition = "TEXT")
    private String RequestMessage;

    @Enumerated(EnumType.STRING)
    private RequestStatus RequestStatus;

    @ManyToOne
    @JoinColumn(name = "OrgID", referencedColumnName = "OrgID", insertable = false, updatable = false)
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "InfluencerID", referencedColumnName = "InfluencerID", insertable = false, updatable = false)
    private Influencer influencer;
}

enum RequestStatus {
    Pending,
    Accepted,
    Denied
}