package io.rest.monitor.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_data", schema = "public")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "user_id")
	private Integer userId;
	@Column(name = "user_name")
	private String userName;
	@Column(name = "user_surname")
	private String userSurname;
	@Column(name = "user_email")
	private String userEmail;
	@Column(name = "user_address")
	private String userAddress;
	
	public UserEntity(Integer userId, String userName, String userSurname, String userEmail, String userAddress) {
		this.userId = userId;
		this.userName = userName;
		this.userSurname = userSurname;
		this.userEmail = userEmail;
		this.userAddress = userAddress;
	}
	
	public UserEntity() {}


	public Integer getUserId() {
		return userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserSurname() {
		return userSurname;
	}
	public void setUserSurname(String userSurname) {
		this.userSurname = userSurname;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
}
