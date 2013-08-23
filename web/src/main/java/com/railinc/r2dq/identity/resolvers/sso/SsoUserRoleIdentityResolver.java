package com.railinc.r2dq.identity.resolvers.sso;

import static com.google.common.collect.Collections2.transform;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.sso.SingleSignOn;
import com.railinc.r2dq.util.SimpleExceptionListener;

@Component
public class SsoUserRoleIdentityResolver implements SsoUserIdentityResolver {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private SingleSignOn singleSignOn;
	
	public SsoUserRoleIdentityResolver() {}
	public SsoUserRoleIdentityResolver(SingleSignOn sso) {
		this.singleSignOn = sso;
	}

	@Required
	public void setSingleSignOn(SingleSignOn singleSignOn) {
		this.singleSignOn = singleSignOn;
	}

	@Override
	public Collection<Identity> apply(String userLogin) {
		log.debug("Resolve Identities for {}", userLogin);
		if (isBlank(userLogin)) {
			return Collections.emptyList();
		}
		
		SimpleExceptionListener sel = new SimpleExceptionListener();
		Collection<String> groupsForUser = this.singleSignOn.groupsForUser(userLogin, sel);
		log.debug("Resolved {} for {}", groupsForUser, userLogin);
		return transform(groupsForUser, new Function<String,Identity>() {
			@Override
			public Identity apply(String input) {
				return new Identity(IdentityType.SsoRole, input);
			}});
	}

}
