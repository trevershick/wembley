package com.railinc.wembley.api.address;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.StringUtils.lowerCase;

import com.google.common.base.Preconditions;
import com.railinc.wembley.rrn.Rrn;
import com.railinc.wembley.rrn.SmtpRrn;

public class EmailAddress implements Address {

	private String email;
	
	public EmailAddress(String em) {
		Preconditions.checkArgument(isNotBlank(em), "email cannot be blank");
		this.email = lowerCase(em);
	}
	
	public String toString() {
		return this.email;
	}


	@Override
	public Rrn toRrn() {
		return new SmtpRrn(email);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmailAddress other = (EmailAddress) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}
	
	
}
