package com.railinc.r2dq.identity.expander;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;
import java.util.Collections;

import com.railinc.r2dq.domain.Identity;

public class SingularNoopExpander implements IdentityExpander {


	@Override
	public Collection<Identity> expand(Identity id) {
	
		if (id.isSingular()) {
			return newArrayList(id);
		} else {
			return Collections.emptyList();
		}
	}
}
