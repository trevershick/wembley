package com.railinc.r2dq.correspondence;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import com.google.gson.Gson;

public class SimpleContact implements Contact {

	private String email;
	private String fullName;

	public SimpleContact(String fullName, String email) {
		this.fullName = fullName;
		this.email = email;
	}
	public SimpleContact(String email) {
		this.email = email;
	}
	
	@Override
	public boolean hasEmail() {
		return isNotBlank(email);
	}

	@Override
	public String getFullName() {
		return fullName;
	}

	@Override
	public String getEmailAddress() {
		return email;
	}
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

	public static Contact with(String name, String email) {
		return new SimpleContact(name, email);
	}

	public static Contact withEmail(String email) {
		return new SimpleContact(email);
	}
}
