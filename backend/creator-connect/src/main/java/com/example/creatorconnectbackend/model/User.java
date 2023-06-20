package com.example.creatorconnectbackend.model;

import lombok.Data;
import jakarta.persistence.*;


@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name= "UserID")
    private Long userID;

    @Column(unique = true, name = "Email")
    private String email;

    @Column( name = "Password")
    private String password;

    @Column (name = "user_type")
    private String user_type;

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	
	public String printUser(User user) {
		return user.getEmail() + user.getPassword() + user.getUser_type();
	}
    
    
}