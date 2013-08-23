package com.railinc.r2dq.identity.expander;

import java.util.Collection;

import com.railinc.r2dq.domain.Identity;
/**
 * expands an identity by resolving the logical members of the identity
 * if the identity is singular, this does nothing.
 * @author trevershick
 *
 */
public interface IdentityExpander {
	/**
	 * return the expanded identities. if they are not expanded then return an empty list.
	 * @return the list of identities. returns an empty list if id is null
	 * @param id the id to expand.
	 */
	Collection<Identity> expand(Identity id);
}
