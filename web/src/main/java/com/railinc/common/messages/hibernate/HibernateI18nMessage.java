package com.railinc.common.messages.hibernate;

import static org.apache.commons.lang.StringUtils.trimToNull;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import com.google.gson.Gson;
import com.railinc.common.messages.I18nMessage;

@Table(name="MESSAGE_SOURCE")
@Entity(name="messagesource")
@DiscriminatorColumn(name="DATA_TYPE")
@DiscriminatorValue("COMMON")
public class HibernateI18nMessage implements I18nMessage {
	


	
	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_LOCALE = "locale";
	public static final String PROPERTY_KEY = "key";
	public static final String PROPERTY_TEXT = "text";
	
	public static final int MAX_LENGTH_LOCALE = 5;
	public static final int MAX_LENGTH_CODE = 255;
	public static final int MAX_LENGTH_TEXT = 512;
	public static final String PROPERTY_CODE = "code";
	public static final String DEFAULT_ORDER_BY_PROPERTY = PROPERTY_CODE;
	
	@Id
	@SequenceGenerator(name="MESSAGE_SOURCE_SEQ", sequenceName="MESSAGE_SOURCE_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="MESSAGE_SOURCE_SEQ")
	@Column(name="MESSAGE_SOURCE_ID")
	private Long id;
	
	@Basic
	@Column(name="MESSAGE_LOCALE",length=MAX_LENGTH_LOCALE,nullable=true)
	private String locale;
	
	@Basic
	@Column(name="MESSAGE_CODE",length=MAX_LENGTH_CODE,nullable=false)
	private String code;

	
	

	@Version
    @Column(name="VERSION")
    private Integer version;

	@Basic
	@Column(name="MESSAGE_TEXT",length=MAX_LENGTH_TEXT,nullable=false)
	private String text;

	
	public HibernateI18nMessage(){}

	public Integer getVersion() {
		return version;
	}
	
	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}


	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = trimToNull(locale);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HibernateI18nMessage other = (HibernateI18nMessage) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (locale == null) {
			if (other.locale != null)
				return false;
		} else if (!locale.equals(other.locale))
			return false;
		return true;
	}



	


}
