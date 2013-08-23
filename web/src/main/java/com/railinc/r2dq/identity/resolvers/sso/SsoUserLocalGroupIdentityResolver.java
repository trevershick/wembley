package com.railinc.r2dq.identity.resolvers.sso;

import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.domain.UserGroup;
import com.railinc.r2dq.usergroup.UserGroupService;

@Component
public class SsoUserLocalGroupIdentityResolver implements SsoUserIdentityResolver {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private UserGroupService userGroupService;
	

	public void setUserGroupService(UserGroupService userGroupService) {
		this.userGroupService = userGroupService;
	}


	@Override
	public Collection<Identity> apply(String userLogin) {
		log.debug("Resolve Identities for {}", userLogin);
		Collection<UserGroup> groupsForUser = newArrayList();
		
		try {
			 groupsForUser = this.userGroupService.groupsForUser(userLogin);
		} catch (Exception e) {
			log.warn("Exception during resolution", e);
		}
		
		log.debug("Resolved {} for {}", groupsForUser, userLogin);
		return transform(groupsForUser, new Function<UserGroup,Identity>() {
			@Override
			public Identity apply(UserGroup input) {
				return new Identity(IdentityType.LocalGroup, input.getIdentifier());
			}});
	}

}
