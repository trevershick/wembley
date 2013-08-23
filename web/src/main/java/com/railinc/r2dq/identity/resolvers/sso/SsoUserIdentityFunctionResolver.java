package com.railinc.r2dq.identity.resolvers.sso;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.Collection;
import java.util.Collections;

import org.springframework.stereotype.Component;

import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;

@Component
public class SsoUserIdentityFunctionResolver implements SsoUserIdentityResolver {

	@Override
	public Collection<Identity> apply(String userLogin) {
		if (isBlank(userLogin)) {
			return Collections.emptyList();
		}
		return newArrayList(new Identity(IdentityType.SsoId, userLogin));
	}

}
