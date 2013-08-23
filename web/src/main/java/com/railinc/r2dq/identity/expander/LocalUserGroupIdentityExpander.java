package com.railinc.r2dq.identity.expander;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;
import java.util.Collections;

import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.domain.UserGroup;
import com.railinc.r2dq.domain.UserGroupMember;
import com.railinc.r2dq.usergroup.UserGroupService;

public class LocalUserGroupIdentityExpander implements IdentityExpander {

	private UserGroupService userGroupService;

	public LocalUserGroupIdentityExpander() {}
	public LocalUserGroupIdentityExpander(UserGroupService s) {
		this.userGroupService = s;
	}

	public UserGroupService getUserGroupService() {
		return userGroupService;
	}

	public void setUserGroupService(UserGroupService userGroupService) {
		this.userGroupService = userGroupService;
	}

	@Override
	public Collection<Identity> expand(Identity id) {
		if (id == null || id.getType() != IdentityType.LocalGroup) {
			return Collections.emptyList();
		}
		UserGroup userGroup = this.userGroupService.get(id.getId());
		if (userGroup == null) {
			return Collections.emptyList();
		}
		
		Collection<Identity> ids = newArrayList();
		for (UserGroupMember s : userGroup.getMembers()) {
			ids.add(Identity.forSsoId(s.getSsoId()));
		}
		return ids;
	}
}
