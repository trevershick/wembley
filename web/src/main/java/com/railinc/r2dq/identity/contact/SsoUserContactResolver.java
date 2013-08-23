package com.railinc.r2dq.identity.contact;

import org.springframework.beans.factory.annotation.Required;

import com.railinc.r2dq.correspondence.Contact;
import com.railinc.r2dq.correspondence.SimpleContact;
import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.sso.SingleSignOn;
import com.railinc.r2dq.sso.SingleSignOnUser;

public class SsoUserContactResolver implements ContactResolver {

	private SingleSignOn singleSignOn;
	
	@Override
	public Contact apply(Identity id) {
		if (id == null) {return null;}
		if (!id.isSsoUser()) {
			return null;
		}
		SingleSignOnUser user = this.singleSignOn.userByLogin(id.getId(), null);
		if (user != null) {
			return SimpleContact.with(user.getFullName(), user.getEmail());
		}
		return null;
	}

	public SingleSignOn getSingleSignOn() {
		return singleSignOn;
	}

	@Required
	public void setSingleSignOn(SingleSignOn singleSignOn) {
		this.singleSignOn = singleSignOn;
	}

}
