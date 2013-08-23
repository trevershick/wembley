package com.railinc.r2dq.identity;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.tryFind;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.railinc.r2dq.correspondence.Contact;
import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.identity.contact.ContactResolver;
import com.railinc.r2dq.identity.expander.IdentityExpander;
import com.railinc.r2dq.identity.resolvers.email.EmailIdentityResolver;
import com.railinc.r2dq.identity.resolvers.sso.SsoUserIdentityResolver;

public class BasicIdentityService implements IdentityService {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private List<ContactResolver> contactResolver = newArrayList();
	
	private List<SsoUserIdentityResolver> ssoUserResolvers = newArrayList();
	
	private List<EmailIdentityResolver> emailResolvers = newArrayList();
	
	private List<IdentityExpander> identityExpanders = newArrayList();
	
	
	@Required
	public void setSsoUserResolvers(List<SsoUserIdentityResolver> ssoUserResolvers) {
		this.ssoUserResolvers = ssoUserResolvers;
	}
	
	@Required
	public void setEmailResolvers(List<EmailIdentityResolver> emailResolvers) {
		this.emailResolvers = emailResolvers;
	}



	@Override
	public String registerEmail(Identity identity) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Collection<Identity> identitiesForPerson(Identity identity) {
		Preconditions.checkArgument(identity.isSingular(), "%s must be an actual person", identity);
		if (identity.isEmail()) {
			return identitiesByEmail(identity.getId());
		} else if (identity.isSsoUser()) {
			return identitiesBySsoUser(identity.getId());
		} else {
			return Collections.emptyList();
		}
	}
	
	@Override
	public Collection<Identity> identitiesBySsoUser(String userId) {
		return new StringToIdentitiesXFormer(ssoUserResolvers).apply(newArrayList(userId));
	}

	@Override
	public Collection<Identity> identitiesByEmailToken(String emailLinkToken) {
		String email = "what";
		return identitiesByEmail(email);
		// these shoudl expire...
		// conver the emailLinkToken into an email address?
		// then return identities from that email address?
	}
	
	public Collection<Identity> identitiesByEmail(String emailaddress) {
		Collection<Identity> identities = new StringToIdentitiesXFormer(emailResolvers).apply(newArrayList(emailaddress));
		
		// these shoudl expire...
		// conver the emailLinkToken into an email address?
		// then return identities from that email address?
		return identities;
	}

	@Override
	public Contact contactInfo(Identity identity) {
		Preconditions.checkArgument(identity.isSingular(), "%s must be an actual person", identity);
		Contact c = null;
		for (ContactResolver cr : this.contactResolver) {
			c = cr.apply(identity);
			if (c != null) { break; }
		}
		return c;
	}

	@Override
	public Collection<Identity> getCurrentIdentities() {
		R2DQPrincipal principal = (R2DQPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal == null) {
			return Collections.emptyList();
		}
		return principal.identities();
	}

	/**
	 * {@inheritDoc}
	 */
	protected Collection<Identity> eliminateRedundant(Collection<Identity> in) {
		Multimap<IdentityType, Identity> idTypeToIds = Multimaps.index(in, Identity.IDENTITY_TYPE_FUNCTION);
		
		Collection<Identity> ssoIdIdentities = idTypeToIds.get(IdentityType.SsoId);
		Collection<String> emails = transform(idTypeToIds.get(IdentityType.ExternalEmailUser), Identity.ID_FUNCTION);
		
		
		// ex. scenario
		// ssoIdentities = (sdtxs01)
		// iterate over each email.
		//   ex. trever.shick@railinc.com, trevershick@yahoo.com
		//   
		//   get the ssoId identities that are associated to the email
		//      (n=1)
		//      trever.shick@railinc.com => [sdtxs01]
		//      ssoIdentities contains sdtxs01, trever.shick@railinc.com IS REDUNDANT, do NOT add it to the final results
		//      (n=2)
		//      trevershick@yahoo.com => [] (empty list)
		//      ssoIdentities doesn't contain 'null', so  trevershick@yahoo.com is NOT REDUNDANT, add it to the results
		Collection<Identity> results = newHashSet();
		for (String email : emails) {
			Collection<Identity> ssoUserIdsFromEmail = new StringToIdentitiesXFormer(emailResolvers).apply(emails);
			if (Collections.disjoint(ssoUserIdsFromEmail, ssoIdIdentities)) {
				results.add(Identity.forExternalEmailUser(email));
			}
		}
		results.addAll(ssoIdIdentities);
		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Identity> resolveToSingularIdentities(Collection<Identity> usersAndOrGroups) {
		log.info("resolveIdentities({})", usersAndOrGroups);
		Collection<Identity> result = newArrayList();
		
		for (Identity unresolved : usersAndOrGroups) {
			for (IdentityExpander ie : identityExpanders) {
				log.debug("querying expander {}", ie);
				result.addAll(ie.expand(unresolved));
				log.debug("  result currently {}", result);
			}
		}
		return eliminateRedundant(result);
	}

	@Required
	public void setIdentityExpanders(List<IdentityExpander> identityExpanders) {
		this.identityExpanders = identityExpanders;
	}

	public List<ContactResolver> getContactResolver() {
		return contactResolver;
	}

	@Required
	public void setContactResolver(List<ContactResolver> contactResolver) {
		this.contactResolver = contactResolver;
	}

	@Override
	public Identity getMostSpecificSingularIdentity(Collection<Identity> currentIdentities) {
		// prefer sso user id
		Collection<Identity> actualPersons = filter(currentIdentities, new Predicate<Identity>() {
			@Override
			public boolean apply(Identity input) {
				return input.isSingular();
			}});
		if (actualPersons.isEmpty()) {
			return null;
		}
		if (actualPersons.size() == 1) {
			return getFirst(actualPersons, null);
		}
		// find sso user?
		return tryFind(actualPersons, new Predicate<Identity>() {
			@Override
			public boolean apply(Identity input) {
				return input.getType() == IdentityType.SsoId;
			}}).or(getFirst(actualPersons, null));
	}

}
