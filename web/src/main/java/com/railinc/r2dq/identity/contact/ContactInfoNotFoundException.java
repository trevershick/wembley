package com.railinc.r2dq.identity.contact;

import com.railinc.r2dq.domain.Identity;

public class ContactInfoNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3069521829533093227L;
	/**
	 * 
	 */
	private final Identity identity;
	
	public ContactInfoNotFoundException(Identity id) {
		super(String.format("Unable to find contact information for %s", id.getId()));
		this.identity = id;
	}
	

	public Identity getIdentity() {
		return identity;
	}

}
