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
@XmlType(name = "ApplicationMailinglistType",
		namespace="http://events.notifserv.railinc.com", propOrder={"application", "type"})
public class ApplicationMailinglistVo extends AbstractMailinglistDeliverySpecVo implements Serializable {

	private static final long serialVersionUID = -1274298706265896110L;

	@XmlElement(namespace="http://events.notifserv.railinc.com")
	private String application;
	@XmlElement(namespace="http://events.notifserv.railinc.com")
	private String type;
	
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object obj) {
		
		if(obj == this) {
			return true;
		}

		if ( obj == null || !( obj instanceof ApplicationMailinglistVo ) ) {
			return false;
		}

		ApplicationMailinglistVo mailingList = (ApplicationMailinglistVo)obj;

		return (application == null ? mailingList.application == null : application.equals( mailingList.application ) ) &&
			(this.type == null ? mailingList.type == null : this.type.equals( mailingList.type ) );
	}

	@Override
	public int hashCode() {
		return (application == null ? 0 : application.hashCode()) +
				(29 * (type == null ? 0 : type.hashCode()));
	}

	@Override
	public String toString() {
		return String.format( "Application Mailinglist: Application=%s, Type=%s", application, type );
	}

	public List<String> validate() {
		List<String> msgs = new ArrayList<String>();

		if ( StringUtils.isEmpty( application ) ) {
			msgs.add( "Missing Application" );
		}

		if ( StringUtils.isEmpty( type ) ) {
			msgs.add( "Missing Type" );
		}

		return msgs;
	}
}
