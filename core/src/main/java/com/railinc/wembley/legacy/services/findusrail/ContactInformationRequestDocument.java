package com.railinc.wembley.legacy.services.findusrail;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ContactInformationRequestDocument", namespace=FindUsRailContactServiceImpl.REQUEST_NS)
@XmlAccessorType(XmlAccessType.FIELD)
public class ContactInformationRequestDocument {

	@XmlElement(name="MessageHeader", namespace=FindUsRailContactServiceImpl.REQUEST_NS)
	private MessageHeader messageHeader;
	@XmlElement(name="ContactRequestDetail", namespace=FindUsRailContactServiceImpl.REQUEST_NS)
	private ContactRequestDetail contactRequestDetail;

	public ContactRequestDetail getContactRequestDetail() {
		return contactRequestDetail;
	}
	public void setContactRequestDetail(ContactRequestDetail contactRequestDetail) {
		this.contactRequestDetail = contactRequestDetail;
	}
	public MessageHeader getMessageHeader() {
		return messageHeader;
	}
	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}
}
