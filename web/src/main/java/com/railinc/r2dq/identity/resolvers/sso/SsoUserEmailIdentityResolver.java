package com.railinc.r2dq.identity.resolvers.sso;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.sso.SingleSignOn;
import com.railinc.r2dq.util.SimpleExceptionListener;
/**
 * Resolves a given sso user id to an 'external email address' identity.
 * In this way, if I login and a task is assigned to 'External Email User'/'trever.shick@railinc.com',
 * i'll be able to perform this task.
 * 
 * @author trevershick
 */
public class SsoUserEmailIdentityResolver implements SsoUserIdentityResolver {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private SingleSignOn singleSignOn;
	
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
		String email = this.singleSignOn.emailForUser(userLogin, sel);
		if (isBlank(email)) {
			return Collections.emptyList();
		}
		
		log.debug("Resolved {} for {}", email, userLogin);
		return newArrayList(new Identity(IdentityType.ExternalEmailUser, email));
	}

}
