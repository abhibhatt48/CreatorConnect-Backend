package com.example.creatorconnectbackend.model;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "social_media")
public class SocialMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long SMID;

    private Long UserID;

    private String Platform;

    private String Link;

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "UserID", insertable = false, updatable = false)
    private User user;
}
