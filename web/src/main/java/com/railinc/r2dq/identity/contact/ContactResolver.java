package com.railinc.r2dq.identity.contact;

import com.google.common.base.Function;
import com.railinc.r2dq.correspondence.Contact;
import com.railinc.r2dq.domain.Identity;

public interface ContactResolver extends Function<Identity,Contact>{
	/**
	 * @param id
	 * @return null if a contact can't be constructed
	 */
	Contact apply(Identity id);
}
