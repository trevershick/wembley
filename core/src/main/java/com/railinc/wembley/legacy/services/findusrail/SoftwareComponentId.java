package com.railinc.wembley.legacy.services.findusrail;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
public class SoftwareComponentId {

	@XmlAttribute(name="IDSchemeNameText", namespace=FindUsRailContactServiceImpl.REQUEST_NS)
	private String idSchemeName;
	@XmlValue
	private String softwareComponentId;

	public String getIdSchemeName() {
		return idSchemeName;
	}
	public void setIdSchemeName(String idSchemeName) {
		this.idSchemeName = idSchemeName;
	}
	public String getSoftwareComponentId() {
		return softwareComponentId;
	}
	public void setSoftwareComponentId(String softwareComponentId) {
		this.softwareComponentId = softwareComponentId;
	}
}
