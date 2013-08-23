package com.railinc.wembley.api.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SSOUserType",
		namespace="http://events.notifserv.railinc.com")
public class SSOUserIdVo implements Serializable {

	private static final long serialVersionUID = 8738916482124397792L;
	@XmlElement(namespace="http://events.notifserv.railinc.com")
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}

		if(obj == null || !(obj instanceof SSOUserIdVo)) {
			return false;
		}

		SSOUserIdVo user = (SSOUserIdVo)obj;

		return (userId == null ? user.userId == null : userId.equals(user.userId));
	}

	@Override
	public int hashCode() {
		return userId == null ? 0 : userId.hashCode();
	}

	@Override
	public String toString() {
		return String.format("SSO UserId=%s", userId);
	}

	public List<String> validate() {
		List<String> msgs = new ArrayList<String>();

		if(StringUtils.isEmpty(this.userId)) {
			msgs.add("Missing SSO User ID");
		}

		return msgs;
	}
}
