package com.railinc.wembley.legacy.services.findusrail.model;

import java.io.Serializable;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.railinc.wembley.api.findusrail.FindUsRailContactCategoryRole;
import com.railinc.wembley.api.findusrail.FindUsRailContactCompanyType;

public class FindUsRailContactRequestMsg implements FindUsRailContactRequest, Serializable {

	private static final long serialVersionUID = -4647386703301660507L;
	private FindUsRailContactCompanyType companyType;
	private String companyId;
	private String category;
	private FindUsRailContactCategoryRole categoryRole;
	private String categoryFunction;
	private boolean includeAgents;
	private boolean includeChildren;
	private boolean includeParent;

	public String getCategoryFunction() {
		return categoryFunction;
	}
	public void setCategoryFunction(String categoryFunction) {
		this.categoryFunction = categoryFunction;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public FindUsRailContactCategoryRole getCategoryRole() {
		return categoryRole;
	}
	public void setContactType(FindUsRailContactCategoryRole categoryRole) {
		this.categoryRole = categoryRole;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setMark(String companyId) {
		this.companyId = companyId;
	}
	public FindUsRailContactCompanyType getCompanyType() {
		return companyType;
	}
	public void setCompanyType(FindUsRailContactCompanyType companyType) {
		this.companyType = companyType;
	}
	public boolean isIncludeAgents() {
		return includeAgents;
	}
	public void setIncludeAgents(boolean includeAgents) {
		this.includeAgents = includeAgents;
	}
	public boolean isIncludeChildren() {
		return includeChildren;
	}
	public void setIncludeChildren(boolean includeChildren) {
		this.includeChildren = includeChildren;
	}
	public boolean isIncludeParent() {
		return includeParent;
	}
	public void setIncludeParent(boolean includeParent) {
		this.includeParent = includeParent;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}

		if(obj == null || !(obj instanceof FindUsRailContactRequestMsg)) {
			return false;
		}

		FindUsRailContactRequestMsg request = (FindUsRailContactRequestMsg)obj;
		return ObjectUtils.equals(companyType, request.companyType) &&
			StringUtils.equals(this.companyId, request.companyId) &&
			StringUtils.equals(this.category, request.category) &&
			ObjectUtils.equals(this.categoryRole, request.categoryRole) &&
			StringUtils.equals(this.categoryFunction, request.categoryFunction);
	}

	@Override
	public int hashCode() {
		return (this.companyType == null ? 0 : this.companyType.hashCode()) +
			(29 * StringUtils.defaultString(this.companyId).hashCode()) +
			(29 * StringUtils.defaultString(this.category).hashCode()) +
			(29 * (this.categoryRole == null ? 0 : this.categoryRole.hashCode())) +
			(29 * StringUtils.defaultString(this.categoryFunction).hashCode());
	}

	@Override
	public String toString() {
		return String.format("FindUsRail Request: %s:%s->%s:%s->%s (%s|%s|%s)", companyType, companyId, category, categoryRole, categoryFunction,
				String.valueOf(includeAgents), String.valueOf(includeChildren), String.valueOf(includeParent));
	}
}
