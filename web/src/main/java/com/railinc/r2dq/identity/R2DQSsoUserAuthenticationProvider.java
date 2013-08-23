package com.railinc.r2dq.identity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.railinc.r2dq.identity.R2DQPrincipal.SsoUserPrincipal;
import com.railinc.sso.rt.LoggedUser;
/**
 * populates the principal object
 * @author trevershick
 *
 */
public class R2DQSsoUserAuthenticationProvider implements AuthenticationProvider {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private IdentityService identityService;
	
	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		Object details = auth.getDetails();
		log.debug("authentication.details={}", details);
		if (details == null || (!(details instanceof LoggedUser))) {
			return null;
		}
		
		LoggedUser lu = (LoggedUser) auth.getDetails();
		SsoUserPrincipal r2dqPrincipal = new R2DQPrincipal.SsoUserPrincipal(lu, identityService);
		
		return new PreAuthenticatedAuthenticationToken(r2dqPrincipal, 
				auth.getCredentials(),
				r2dqPrincipal.authorities());
	}
	



	@Override
	public boolean supports(Class<?> authentication) {
		return PreAuthenticatedAuthenticationToken.class == authentication;
	}
	
	@Required
	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}

}
