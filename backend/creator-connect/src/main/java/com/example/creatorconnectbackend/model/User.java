package com.example.creatorconnectbackend.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import jakarta.persistence.*;


@Data
@Entity
@Table(name = "users")
public class User {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column (name= "UserID")
	    private Long userID;

	    @Email
	    @NotNull
	    @Pattern(regexp=".+@.+\\..+", message="Invalid email address")
	    @Column(unique = true, name = "Email")
	    private String email;

	    @NotNull
	    @Size(min=8) // If you want to enforce a minimum password length of 8
	    @Column( name = "Password")
	    private String password;

	    @NotNull
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