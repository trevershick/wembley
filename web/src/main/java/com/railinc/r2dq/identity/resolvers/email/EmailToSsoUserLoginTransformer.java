package com.railinc.r2dq.identity.resolvers.email;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Function;
import com.railinc.r2dq.sso.SingleSignOn;
import com.railinc.r2dq.util.SimpleExceptionListener;

public class EmailToSsoUserLoginTransformer implements Function<String,Collection<String>>{
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private SingleSignOn singleSignOn;
	
	@Required
	public void setSingleSignOn(SingleSignOn singleSignOn) {
		this.singleSignOn = singleSignOn;
	}


	@Override
	public Collection<String> apply(String email) {
		log.debug("Attempting to get user logins for email {}", email);
		
		SimpleExceptionListener sel = new SimpleExceptionListener(log);
		Collection<String> userLoginsByEmail = singleSignOn.userLoginsByEmail(email, sel);
		log.debug("Returned email {}", userLoginsByEmail);
		
		return userLoginsByEmail;
	}

}
