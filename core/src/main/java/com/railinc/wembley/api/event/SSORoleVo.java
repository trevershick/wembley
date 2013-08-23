package com.railinc.wembley.api.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SSORoleType",
		namespace="http://events.notifserv.railinc.com", propOrder={"mark", "role"})
public class SSORoleVo implements Serializable {

	private static final long serialVersionUID = -3397810463289214865L;
	@XmlElement(namespace="http://events.notifserv.railinc.com")
	private String mark;
	@XmlElement(namespace="http://events.notifserv.railinc.com")
	private String role;

	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}

		if(obj == null || !(obj instanceof SSORoleVo)) {
			return false;
		}

		SSORoleVo role = (SSORoleVo)obj;

		return (mark == null ? role.mark == null : mark.equals(role.mark)) &&
			(this.role == null ? role.role == null : this.role.equals(role.role));
	}

	@Override
	public int hashCode() {
		return (mark == null ? 0 : mark.hashCode()) +
				(29 * (role == null ? 0 : role.hashCode()));
	}

	@Override
	public String toString() {
		return String.format("SSO Role: Mark=%s, Role=%s", mark, role);
	}

	public List<String> validate() {
		List<String> msgs = new ArrayList<String>();

		if(StringUtils.isEmpty(role)) {
			msgs.add("Missing SSO Role");
		}

		return msgs;
	}
}
