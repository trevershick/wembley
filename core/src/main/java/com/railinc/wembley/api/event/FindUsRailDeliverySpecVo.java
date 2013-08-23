package com.railinc.wembley.api.event;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindUsRailDeliverySpecType", namespace="http://events.notifserv.railinc.com")
public class FindUsRailDeliverySpecVo extends BaseEmailDeliverySpecVo {

	private static final long serialVersionUID = 6270319030752204275L;

	@XmlElementWrapper(name="findUsRailContacts", namespace="http://events.notifserv.railinc.com")
	@XmlElement(name="contact", namespace="http://events.notifserv.railinc.com")
	private List<FindUsRailContactVo> contacts;

	public List<FindUsRailContactVo> getContacts() {
		return contacts;
	}

	public void setContacts(List<FindUsRailContactVo> contacts) {
		this.contacts = contacts;
	}

	@Override
	public String toString() {
		return String.format("FindUsRailDeliverySpec: From=%s, Subject=%s, ContentType=%s, To=%s", getFrom(), getSubject(), getContentType(), contacts);
	}

	@Override
	public List<String> validate() {
		List<String> msgs = super.validate();
		if(contacts == null || contacts.size() == 0) {
			msgs.add("No FindUsRail Contacts");
		} else {
			for(FindUsRailContactVo contact : this.contacts) {
				if(contact != null) {
					msgs.addAll(contact.validate());
				}
			}
		}
		return msgs;
	}
}
