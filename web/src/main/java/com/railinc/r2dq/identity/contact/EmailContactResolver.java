package com.railinc.r2dq.identity.contact;

import static com.google.common.collect.Iterables.getFirst;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;

import com.railinc.r2dq.correspondence.Contact;
import com.railinc.r2dq.correspondence.SimpleContact;
import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.sso.SingleSignOn;
import com.railinc.r2dq.sso.SingleSignOnUser;

public class EmailContactResolver implements ContactResolver {

	private SingleSignOn singleSignOn;
	
	@Override
	public Contact apply(Identity id) {
		if (id == null) {return null;}
		if (!id.isEmail()) {
			return null;
		}
		Collection<SingleSignOnUser> usersByEmail = this.singleSignOn.usersByEmail(id.getId(), null);
		if (! usersByEmail.isEmpty()) {
			SingleSignOnUser u = getFirst(usersByEmail, null);
			return SimpleContact.with(u.getFullName(), id.getId());
		} else {
			return SimpleContact.withEmail(id.getId());
		}
	}

	public SingleSignOn getSingleSignOn() {
		return singleSignOn;
	}

	@Required
	public void setSingleSignOn(SingleSignOn singleSignOn) {
		this.singleSignOn = singleSignOn;
	}

}
