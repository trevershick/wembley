package com.railinc.wembley.legacy.domain.impl.sso;

public class SSOUserInfo {
	private String email;
	private String username;
	private String status; // assume it's active if we aren't told otherwise.
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String toString() {
		return new StringBuilder("SSORestUser [userId=")
			.append(username)
			.append(", email=")
			.append(email)
			.append(", status=")
			.append(status)
			.append("]").toString();
	}
	
}
