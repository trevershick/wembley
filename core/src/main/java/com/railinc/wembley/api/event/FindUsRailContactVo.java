package com.railinc.wembley.api.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;

import com.railinc.wembley.api.findusrail.FindUsRailContactCategoryRole;
import com.railinc.wembley.api.findusrail.FindUsRailContactCompanyType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindUsRailContactType",
		namespace="http://events.notifserv.railinc.com", propOrder={"companyType", "companyId", "category", "categoryRole", "categoryFunction",
				"includeAgents", "includeChildren", "includeParent"})
public class FindUsRailContactVo implements Serializable {

	private static final long serialVersionUID = 366056623119377636L;
	@XmlElement(namespace="http://events.notifserv.railinc.com")
	private String companyType;
	@XmlElement(namespace="http://events.notifserv.railinc.com")
	private String companyId;
	@XmlElement(namespace="http://events.notifserv.railinc.com")
	private String category;
	@XmlElement(namespace="http://events.notifserv.railinc.com")
	private String categoryRole;
	@XmlElement(namespace="http://events.notifserv.railinc.com")
	private String categoryFunction;
	@XmlElement(namespace="http://events.notifserv.railinc.com")
	private Boolean includeAgents;
	@XmlElement(namespace="http://events.notifserv.railinc.com")
	private Boolean includeChildren;
	@XmlElement(namespace="http://events.notifserv.railinc.com")
	private Boolean includeParent;

	public String getCompanyType() {
		return companyType;
	}
	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCategoryRole() {
		return categoryRole;
	}
	public void setCategoryRole(String contactType) {
		this.categoryRole = contactType;
	}
	public String getCategoryFunction() {
		return categoryFunction;
	}
	public void setCategoryFunction(String categoryFunction) {
		this.categoryFunction = categoryFunction;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String mark) {
		this.companyId = mark;
	}
	public Boolean isIncludeAgents() {
		return includeAgents;
	}
	public void setIncludeAgents(Boolean includeAgents) {
		this.includeAgents = includeAgents;
	}
	public Boolean isIncludeChildren() {
		return includeChildren;
	}
	public void setIncludeChildren(Boolean includeChildren) {
		this.includeChildren = includeChildren;
	}
	public Boolean isIncludeParent() {
		return includeParent;
	}
	public void setIncludeParent(Boolean includeParent) {
		this.includeParent = includeParent;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}

		if(obj == null || !(obj instanceof FindUsRailContactVo)) {
			return false;
		}

		FindUsRailContactVo contact = (FindUsRailContactVo)obj;
		return StringUtils.equals(this.companyType, contact.companyType) &&
			StringUtils.equals(this.companyId, contact.companyId) &&
			StringUtils.equals(this.category, contact.category) &&
			StringUtils.equals(this.categoryRole, contact.categoryRole) &&
			StringUtils.equals(this.categoryFunction, contact.categoryFunction);
	}

	@Override
	public int hashCode() {
		return StringUtils.defaultString(this.companyType).hashCode() +
			(29 * StringUtils.defaultString(this.companyId).hashCode()) +
			(29 * StringUtils.defaultString(this.category).hashCode()) +
			(29 * StringUtils.defaultString(this.categoryRole).hashCode()) +
			(29 * StringUtils.defaultString(this.categoryFunction).hashCode());
	}

	@Override
	public String toString() {
		return String.format("FindUsRail: Type=%s, Company=%s, Category=%s, Role=%s, Function=%s, Agents=%s, Children=%s, Parent=%s",
				companyType, companyId, category, categoryRole, categoryFunction, includeAgents, includeChildren, includeParent);
	}

	public List<String> validate() {
		List<String> msgs = new ArrayList<String>();
		if(StringUtils.isEmpty(this.companyType)) {
			msgs.add("Missing Company Type");
		} else {
			try {
				FindUsRailContactCompanyType.valueOf(this.companyType);
			} catch (Throwable e) {
				msgs.add(String.format("Invalid Company Type: %s", this.companyType));
			}
		}

		if(StringUtils.isEmpty(this.companyId)) {
			msgs.add("Missing Company ID");
		}

		if(StringUtils.isEmpty(category)) {
			msgs.add("Missing Category");
		}

		if(StringUtils.isNotEmpty(this.categoryRole)) {
			try {
				FindUsRailContactCategoryRole.valueOf(this.categoryRole);
			} catch (Throwable e) {
				msgs.add(String.format("Invalid Category Role: %s", this.categoryRole));
			}
		}

		return msgs;
	}
}
