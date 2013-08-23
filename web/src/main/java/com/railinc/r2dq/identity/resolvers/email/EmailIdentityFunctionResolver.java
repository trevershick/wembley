package com.railinc.r2dq.identity.resolvers.email;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.Collection;
import java.util.Collections;

import org.springframework.stereotype.Component;

import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;

@Component
public class EmailIdentityFunctionResolver implements EmailIdentityResolver {

	@Override
	public Collection<Identity> apply(String email) {
		if (isBlank(email)) {
			return Collections.emptyList();
		}
		return newArrayList(new Identity(IdentityType.ExternalEmailUser, email));
	}
}
