
package com.railinc.r2dq.identity;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.gson.Gson;
import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;
import com.railinc.sso.rt.LoggedUser;
import com.railinc.sso.rt.Permission;

public abstract class R2DQPrincipal implements Principal, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5618930800406944324L;
	
	protected final Collection<Identity> identities = newHashSet();
	protected final Collection<GrantedAuthority> authorities = newHashSet();
	protected String principalName, userId, email, firstName, lastName, employer;
	
	public Collection<Identity> identities() {
		return Collections.unmodifiableCollection(this.identities);
	}
	public Collection<GrantedAuthority> authorities() {
		return Collections.unmodifiableCollection(this.authorities);
	}
	
	public void add(Identity i) {
		this.identities.add(i);
	}
	
	public boolean hasNoIdentity() {
		return this.identities.isEmpty();
	}
	

	public static class EmailTokenPrincipal extends R2DQPrincipal {
		/**
		 * 
		 */
		private static final long serialVersionUID = -2920647692050528982L;
		
		private final String token;
		
		public EmailTokenPrincipal(String token,IdentityService identityService) {
			this.token = token;
			this.principalName = token;
			this.identities.add(new Identity(IdentityType.ExternalEmailUser, token /*should be email*/));
			this.populate(identityService);
		}
		public void populate(IdentityService identityService) {
			addAll(identityService.identitiesByEmailToken(token));
		}

	}
	public static class SsoUserPrincipal extends R2DQPrincipal {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5788503185667865401L;
		
		private final String userLogin;
		
		public SsoUserPrincipal(LoggedUser lu, IdentityService identityService) {
			this.userLogin = lu.getUserId();
			this.userId = lu.getUserId();
			this.principalName = lu.getUserId();
			this.firstName = lu.getFirstName();
			this.lastName = lu.getLastName();
			this.email = lu.getEmailAddress();
			if (isNotBlank(this.email)) {
				identities.add(new Identity(IdentityType.ExternalEmailUser, this.email));
			}
			this.employer = lu.getEmployer();
			
			this.identities.add(new Identity(IdentityType.SsoId, lu.getUserId()));
			this.authorities.addAll(buildAuthorities(lu));
			populate(identityService);
		}
		
		private Collection<GrantedAuthority> buildAuthorities(LoggedUser user) {
			// Create a list of grants for this user
			Collection<GrantedAuthority> authorities = newArrayList();
			Collection<Permission> permissions = user.getPermissions();
			for (Permission p : permissions) {
				String roleId = p.getRoleId();
				authorities.add(new SimpleGrantedAuthority(roleId));
				Collection<String> marks = p.getEntities();
				for (String mark : marks) {
					authorities.add(new SimpleGrantedAuthority(roleId + "@" + mark));
				}
			}
			return authorities;
		}

		
		public void populate(IdentityService identityService) {
			addAll(identityService.identitiesBySsoUser(userLogin));
		}


		
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	@Override
	public String getName() {
		return this.principalName;
	}

	public abstract void populate(IdentityService identityService);
	
	public void addAll(Collection<Identity> ids) {
		for (Identity id : ids) {
			if (id.getType().isSsoRole()) {
				this.authorities.add(new SimpleGrantedAuthority(id.getId()));
			}
		}
	}
	public String getUserId() {
		return userId;
	}
	public String getEmail() {
		return email;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getEmployer() {
		return employer;
	}
}
