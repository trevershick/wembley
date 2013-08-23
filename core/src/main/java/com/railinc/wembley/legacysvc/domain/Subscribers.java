package com.railinc.wembley.legacysvc.domain;

import java.util.HashSet;

public class Subscribers extends HashSet<Subscriber> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8103042821905989475L;

	public Subscriber byUid(String realm, String uid) {
		if (null == uid || null == realm) {
			return null;
		}
		for (Subscriber s : this) {
			if (realm.equals(s.realm()) && uid.equals(s.uid())) {
				return s;
			}
		}
		return null;
	}
}
