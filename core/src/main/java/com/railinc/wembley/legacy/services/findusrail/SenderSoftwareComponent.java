package com.railinc.wembley.legacy.services.findusrail;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class SenderSoftwareComponent {

	@XmlElement(name="SoftwareComponentID", namespace=FindUsRailContactServiceImpl.REQUEST_NS)
	private SoftwareComponentId softwareComponentId;
	@XmlElement(name="SoftwareVersion", namespace=FindUsRailContactServiceImpl.REQUEST_NS)
	private String softwareVersion;

	public SoftwareComponentId getSoftwareComponentId() {
		return softwareComponentId;
	}
	public void setSoftwareComponentId(SoftwareComponentId softwareComponentId) {
		this.softwareComponentId = softwareComponentId;
	}
	public String getSoftwareVersion() {
		return softwareVersion;
	}
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}
}
