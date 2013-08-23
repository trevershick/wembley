package com.railinc.r2dq.identity;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

import java.util.Collection;

import com.google.common.base.Function;
import com.railinc.r2dq.domain.Identity;

public class StringToIdentitiesXFormer implements Function<Collection<String>, Collection<Identity>>{
	private Collection<? extends Function<String, Collection<Identity>>> resolvers = newArrayList();

	public StringToIdentitiesXFormer(Collection<? extends Function<String, Collection<Identity>>> resolvers) {
		this.resolvers = resolvers;
	}
	
	public Collection<Identity> apply(Collection<String> userLogins) {
		Collection<Identity> ids = newHashSet();
		for (String userLogin : userLogins) {
			for (Function<String, Collection<Identity>> r : resolvers){ 
				Collection<Identity> resolve = r.apply(userLogin);
				ids.addAll(resolve);
			}
		}
		return ids;
	}
	
}
