package com.railinc.wembley.legacy.services.findusrail;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class MessageHeader {

	@XmlElement(name="ControlInformation", namespace=FindUsRailContactServiceImpl.REQUEST_NS)
	private ControlInformation controlInformation;
	@XmlElement(name="SenderSoftwareComponent", namespace=FindUsRailContactServiceImpl.REQUEST_NS)
	private SenderSoftwareComponent senderSoftwareComponent;

	public ControlInformation getControlInformation() {
		return controlInformation;
	}
	public void setControlInformation(ControlInformation controlInformation) {
		this.controlInformation = controlInformation;
	}
	public SenderSoftwareComponent getSenderSoftwareComponent() {
		return senderSoftwareComponent;
	}
	public void setSenderSoftwareComponent(SenderSoftwareComponent senderSoftwareComponent) {
		this.senderSoftwareComponent = senderSoftwareComponent;
	}
}
