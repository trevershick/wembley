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
@XmlType(name = "NamedMailinglistType",
		namespace="http://events.notifserv.railinc.com", propOrder={"shortName"})
public class NamedMailinglistVo extends AbstractMailinglistDeliverySpecVo implements Serializable {

	private static final long serialVersionUID = -1564461502954449296L;

	@XmlElement(namespace="http://events.notifserv.railinc.com")
	private String shortName;

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Override
	public boolean equals(Object obj) {
		
		if(obj == this) {
			return true;
		}

		if ( obj == null || !( obj instanceof NamedMailinglistVo ) ) {
			return false;
		}

		NamedMailinglistVo mailingList = (NamedMailinglistVo)obj;

		return (shortName == null ? mailingList.shortName == null : shortName.equals( mailingList.shortName ) );
	}

	@Override
	public int hashCode() {
		return (shortName == null ? 0 : shortName.hashCode());
	}

	@Override
	public String toString() {
		return String.format( "Named Mailinglist: ShortName=%s", shortName  );
	}

	public List<String> validate() {
		List<String> msgs = new ArrayList<String>();

		if ( StringUtils.isEmpty( shortName ) ) {
			msgs.add( "Missing short name" );
		}
		
		return msgs;
	}
}
