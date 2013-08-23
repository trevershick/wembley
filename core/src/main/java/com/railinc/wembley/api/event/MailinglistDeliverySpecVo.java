package com.railinc.wembley.api.event;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MailinglistDeliverySpecType", namespace="http://events.notifserv.railinc.com")
public class MailinglistDeliverySpecVo extends BaseEmailDeliverySpecVo {

	private static final long serialVersionUID = -139382343973653732L;

	@XmlElementWrapper(name="mailingLists", namespace="http://events.notifserv.railinc.com")
	@XmlElement(name="mailingList", namespace="http://events.notifserv.railinc.com")
	private List<AbstractMailinglistDeliverySpecVo> mailingLists;

	public List<AbstractMailinglistDeliverySpecVo> getMailingLists() {
		return mailingLists;
	}

	public void setMailingLists(List<AbstractMailinglistDeliverySpecVo> mailingLists) {
		this.mailingLists = mailingLists;
	}
	
	@Override
	public String toString() {
		return String.format("MailinglistDeliverySpec: From=%s, Subject=%s, ContentType=%s, To=%s", getFrom(), getSubject(), getContentType(), mailingLists);
	}

	@Override
	public List<String> validate() {

		List<String> msgs = new ArrayList<String>();
		
		if ( mailingLists == null || mailingLists.size() == 0 ) {
			msgs.add( "No Mailinglists" );
		}
		else {
		
			for ( AbstractMailinglistDeliverySpecVo mailingList : this.mailingLists ) {
				
				if ( mailingList != null ) {
					msgs.addAll( mailingList.validate() );
				}
			}
		}
		return msgs;
	}
}
