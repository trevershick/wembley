package com.railinc.wembley.api.event;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SSOUserDeliverySpecType", namespace="http://events.notifserv.railinc.com")
public class SSOUserDeliverySpecVo extends BaseEmailDeliverySpecVo {

	private static final long serialVersionUID = -7913928595779278505L;
	@XmlElementWrapper(name="ssoUserIds", namespace="http://events.notifserv.railinc.com")
	@XmlElement(name="ssoUserId", namespace="http://events.notifserv.railinc.com")
	private List<SSOUserIdVo> userIds;

	public List<SSOUserIdVo> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<SSOUserIdVo> userIds) {
		this.userIds = userIds;
	}

	@Override
	public String toString() {
		return String.format("SSOUserDeliverySpec: From=%s, Subject=%s, ContentType=%s, To=%s", getFrom(), getSubject(), getContentType(), userIds);
	}

	@Override
	public List<String> validate() {
		List<String> msgs = super.validate();
		if(userIds == null || userIds.size() == 0) {
			msgs.add("No SSO User IDs");
		} else {
			for(SSOUserIdVo userId : this.userIds) {
				if(userId != null) {
					msgs.addAll(userId.validate());
				}
			}
		}
		return msgs;
	}
}
