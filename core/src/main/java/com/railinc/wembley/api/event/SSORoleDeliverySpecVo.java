package com.railinc.wembley.api.event;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SSORoleDeliverySpecType", namespace="http://events.notifserv.railinc.com")
public class SSORoleDeliverySpecVo extends BaseEmailDeliverySpecVo {

	private static final long serialVersionUID = -2634704489745728454L;
	@XmlElementWrapper(name="ssoRoles", namespace="http://events.notifserv.railinc.com")
	@XmlElement(name="ssoRole", namespace="http://events.notifserv.railinc.com")
	private List<SSORoleVo> ssoRoles;

	public List<SSORoleVo> getSsoRoles() {
		return ssoRoles;
	}

	public void setSsoRoles(List<SSORoleVo> ssoRoles) {
		this.ssoRoles = ssoRoles;
	}

	@Override
	public String toString() {
		return String.format("SSORoleDeliverySpec: From=%s, Subject=%s, ContentType=%s, To=%s", getFrom(), getSubject(), getContentType(), ssoRoles);
	}

	@Override
	public List<String> validate() {
		List<String> msgs = super.validate();
		if(ssoRoles == null || ssoRoles.size() == 0) {
			msgs.add("No SSO Roles");
		} else {
			for(SSORoleVo ssoRole : this.ssoRoles) {
				if(ssoRole != null) {
					msgs.addAll(ssoRole.validate());
				}
			}
		}
		return msgs;
	}
}
