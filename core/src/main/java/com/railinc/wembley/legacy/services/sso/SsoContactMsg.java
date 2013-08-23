package com.railinc.wembley.legacy.services.sso;

import java.io.Serializable;

public class SsoContactMsg implements SsoContact, Serializable {

	private static final long serialVersionUID = 1L;

	String emailAddress;
	String userId;
	
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress( String emailAddress ) {
		this.emailAddress = emailAddress;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId( String userId ) {
		this.userId = userId;
	}
	
	@Override
	public String toString() {
		return String.format( "UserId: %s::Email: %s", this.userId, this.emailAddress );
	}
}
