package com.railinc.r2dq.identity;

import java.util.Collection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.railinc.r2dq.correspondence.Contact;
import com.railinc.r2dq.domain.Identity;
@Service
@Transactional
public interface IdentityService {

	
	/**
	 * @param id ONLY an 'actual person', an sso id or email
	 * @return
	 */
	Collection<Identity> identitiesForPerson(Identity id);
	Collection<Identity> identitiesBySsoUser(String userId);
	/**
	 * See - {@link #registerEmail(Identity)}
	 * 
	 * @param emailAddress
	 * @return
	 */
	Collection<Identity> identitiesByEmailToken(String emailAddress);
	/**
	 * @param emailAddress
	 * @return and email link 'token' that can be used to create email link authenticatino objects
	 */
	String registerEmail(Identity identity);

	/**
	 * not sure if this goes here, but takes in various identity objects and returns
	 * a distinct list of contacts from those identities.
	 * @param identities
	 * @return
	 */
	Contact contactInfo(Identity identity);

	/**
	 * Returns the identities associated with the current user.  This uses
	 * Spring Security's SecurityContextHolder, so Spring security must be configured
	 * propertly.
	 * @return
	 */
	Collection<Identity> getCurrentIdentities();
	/**
	 * Resolves all identities by 'singularizing' them.
	 * For LocalGroups, this means returning all users in the local group
	 * For sso role, this will return the sso ids that have the role.
	 * If a singular identity (sso id or email) is passed in then they're returned as is.
	 */
	Collection<Identity> resolveToSingularIdentities(Collection<Identity> usersAndOrGroups);
	Identity getMostSpecificSingularIdentity(Collection<Identity> currentIdentities);

}
