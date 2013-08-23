package com.railinc.wembley.legacy.services.findusrail;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Company {

	@XmlElement(name="CompanyType", namespace=FindUsRailContactServiceImpl.REQUEST_NS)
	private String companyType;
	@XmlElement(name="CompanyID", namespace=FindUsRailContactServiceImpl.REQUEST_NS)
	private String companyId;
	@XmlElement(name="CompanyInclude", namespace=FindUsRailContactServiceImpl.REQUEST_NS)
	private List<String> companyInclude;

	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyType() {
		return companyType;
	}
	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}
	public List<String> getCompanyInclude() {
		return companyInclude;
	}
	public void setCompanyInclude(List<String> companyInclude) {
		this.companyInclude = companyInclude;
	}
	public void addCompanyInclude(String companyInclude) {
		if(this.companyInclude == null) {
			this.companyInclude = new ArrayList<String>();
		}
		this.companyInclude.add(companyInclude);
	}
}
