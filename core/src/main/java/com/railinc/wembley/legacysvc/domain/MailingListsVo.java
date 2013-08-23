package com.railinc.wembley.legacysvc.domain;

import java.util.ArrayList;

public class MailingListsVo extends ArrayList<MailingListVo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1290233947985998254L;

	public MailingListsVo active() {
		MailingListsVo ml = new MailingListsVo();
		for (MailingListVo m : this) {
			if (m.isActive()) {
				ml.add(m);
			}
		}
		return ml;
	}

	public MailingListVo byKey(String mailingListKey) {
		for (MailingListVo m : this) {
			if (m.getKey().equals(mailingListKey)) {
				return m;
			}
		}
		return null;
	}
}
