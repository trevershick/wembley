package com.railinc.wembley.legacy.service;

import java.util.ArrayList;
import java.util.List;

import com.railinc.wembley.legacysvc.domain.NotificationVo;


public class NotificationSearchResults extends ArrayList<NotificationVo> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5375157524255220954L;
	private String token;

	public NotificationSearchResults(List<NotificationVo> res,
			String responseToken) {
		super(res);
		this.token = responseToken;
	}

	public String getToken() {
		return token;
	}
}
