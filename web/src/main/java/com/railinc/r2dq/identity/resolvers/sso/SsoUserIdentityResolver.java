package com.railinc.r2dq.identity.resolvers.sso;

import java.util.Collection;

import com.google.common.base.Function;
import com.railinc.r2dq.domain.Identity;

public interface SsoUserIdentityResolver extends Function<String, Collection<Identity>> {
	/**
	 * reoslve the given sso user id to a collection of identities
	 * @param userLogin
	 * @return
	 */
	Collection<Identity> apply(String userLogin);
}
