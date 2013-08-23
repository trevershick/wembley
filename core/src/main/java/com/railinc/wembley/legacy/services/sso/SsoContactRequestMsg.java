package com.railinc.wembley.legacy.services.sso;

import java.io.Serializable;

public class SsoContactRequestMsg implements SsoContactRequest, Serializable {

	private static final long serialVersionUID = 1L;

	private String mark;
	private String role;
	private String userId;
	private String application;
	
	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getMark() {
		return mark;
	}

	public String getRole() {
		return role;
	}

	public String getUserId() {
		return userId;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Override
	public boolean equals( Object obj ) {
		
		if( obj == this ) {
			return true;
		}

		if ( obj == null || !( obj instanceof SsoContactRequestMsg ) ) {
			return false;
		}

		SsoContactRequestMsg request = (SsoContactRequestMsg)obj;

		return ( mark == null ? request.mark == null : mark.equals( request.mark ) ) &&
			( role == null ? request.role == null : role.equals( request.role ) ) &&
			( this.userId == null ? request.userId == null : this.userId.equals( request.userId ) ) &&
			( application == null ? request.application == null : application.equals( request.application ) );
	}

	@Override
	public int hashCode() {
		
		return ( role == null ? 0 : role.hashCode() ) +
				(29 * (mark == null ? 0 : mark.hashCode())) +
				(29 * (userId == null ? 0 : userId.hashCode())) +
				(29 * (application == null ? 0 : application.hashCode()));
	}

	@Override
	public String toString() {
		return String.format("SSO Request: %s:%s:%s:%s", mark, role, userId, application );
	}
}
