package com.railinc.r2dq.identity.resolvers.email;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.identity.StringToIdentitiesXFormer;
import com.railinc.r2dq.identity.resolvers.sso.SsoUserIdentityResolver;
/**
 * Converts and email address into a list of SSO suers, then calls all wired in
 * sso user identity resolvers to get all identities
 * @author trevershick
 *
 */
@Component
public class EmailNestedIdentityResolver implements EmailIdentityResolver {
	
	private Collection<SsoUserIdentityResolver> resolvers = newArrayList();
	
	private EmailToSsoUserLoginTransformer emailToSsoUserLoginTransformer;
	
	@Required
	public void setResolvers(Collection<SsoUserIdentityResolver> resolvers) {
		this.resolvers = resolvers;
	}

	@Required
	public void setEmailToSsoUserLoginTransformer(EmailToSsoUserLoginTransformer emailToSsoUserLoginTransformer) {
		this.emailToSsoUserLoginTransformer = emailToSsoUserLoginTransformer;
	}

	@Override
	public Collection<Identity> apply(String email) {
		Collection<String> userLogins = emailToSsoUserLoginTransformer.apply(email);
		Collection<Identity> resolved = new StringToIdentitiesXFormer(this.resolvers).apply(userLogins);
		return resolved;
	}

}
