package com.railinc.r2dq.identity.expander;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;
import java.util.Collections;

import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.sso.SingleSignOn;
import com.railinc.r2dq.util.SimpleExceptionListener;

public class SsoRoleIdentityExpander implements IdentityExpander {

	private SingleSignOn singleSignOn;



	public SsoRoleIdentityExpander(SingleSignOn sso) {
		this.singleSignOn = sso;
	}
	public SsoRoleIdentityExpander() {
	}



	@Override
	public Collection<Identity> expand(Identity id) {
		if (id == null || id.getType() != IdentityType.SsoRole) {
			return Collections.emptyList();
		}
		Collection<String> userIdsInGroup = this.singleSignOn.usersInGroup(id.getId() , new SimpleExceptionListener());

		Collection<Identity> userIds = newArrayList();
		for (String userId : userIdsInGroup) {
			userIds.add(Identity.forSsoId(userId));
		}
		return userIds;
	}



	public SingleSignOn getSingleSignOn() {
		return singleSignOn;
	}



	public void setSingleSignOn(SingleSignOn singleSignOn) {
		this.singleSignOn = singleSignOn;
	}
}
