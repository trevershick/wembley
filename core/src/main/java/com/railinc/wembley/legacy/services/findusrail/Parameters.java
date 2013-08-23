package com.railinc.wembley.legacy.services.findusrail;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Parameters {

	@XmlElement(name="Company", namespace=FindUsRailContactServiceImpl.REQUEST_NS)
	private Company company;
	@XmlElement(name="CategoryID", namespace=FindUsRailContactServiceImpl.REQUEST_NS)
	private String categoryId;
	@XmlElement(name="CategoryFunctionID", namespace=FindUsRailContactServiceImpl.REQUEST_NS)
	private String categoryFunction;
	@XmlElement(name="CategoryRole", namespace=FindUsRailContactServiceImpl.REQUEST_NS)
	private String categoryRole;

	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public String getCategoryRole() {
		return categoryRole;
	}
	public void setCategoryRole(String categoryRole) {
		this.categoryRole = categoryRole;
	}
	public String getCategoryFunction() {
		return categoryFunction;
	}
	public void setCategoryFunction(String categoryFunction) {
		this.categoryFunction = categoryFunction;
	}
}
