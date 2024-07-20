package io.rest.monitor.request;

import java.io.Serializable;

public class UserRequestPayload implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String userName;
	private String userSurname;
	private String userEmail;
	private String userAddress;
	
	public UserRequestPayload(String userName, String userSurname, String userEmail, String userAddress) {
		this.userName = userName;
		this.userSurname = userSurname;
		this.userEmail = userEmail;
		this.userAddress = userAddress;
	}
	
	public UserRequestPayload() {}

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
