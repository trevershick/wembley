package com.railinc.common.configuration.hibernate;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.trimToNull;

import java.util.Collection;

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
import javax.persistence.Transient;
import javax.persistence.Version;

import com.google.common.base.Supplier;
import com.google.gson.Gson;
import com.railinc.common.configuration.ConfigurationProperty;
import com.railinc.common.configuration.ConfigurationPropertySupport;

@Table(name="CONFIGURATION")
@Entity(name="configuration")
@DiscriminatorColumn(name="DATA_TYPE")
@DiscriminatorValue("COMMON")
public class HibernateConfigurationProperty implements Supplier<String>, ConfigurationProperty {
	

	public static final int MAX_LENGTH_OUTBOUND_QUEUE = 256;
	public static final int MAX_LENGTH_VALUE = 2048;
	public static final int MAX_LENGTH_DESC = 512;
	public static final int MAX_LENGTH_NAME = 100;
	public static final int MAX_LENGTH_CODE = 20;
	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_CODE = "code";
	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_DESC = "description";
	public static final String PROPERTY_VALUE = "value";

	public static final String DEFAULT_ORDER_BY_PROPERTY = PROPERTY_NAME;
	
	@Transient
	private transient final ConfigurationPropertySupport support = new ConfigurationPropertySupport(this);
	
	@Id
	@SequenceGenerator(name="CONFIGURATION_SEQ", sequenceName="CONFIGURATION_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="CONFIGURATION_SEQ")
	@Column(name="CONFIG_ID")
	private Long id;

	@Basic
    @Column(name="CONFIG_CODE",length=MAX_LENGTH_CODE,nullable=false)
	private String code;

	@Version
    @Column(name="VERSION")
    private Integer version;

	@Basic
	@Column(name="CONFIG_NAME",length=MAX_LENGTH_NAME,nullable=false)
	private String name;

	@Basic
	@Column(name="CONFIG_VALUE",length=MAX_LENGTH_NAME,nullable=false)
	private String value;

	@Basic
	@Column(name="CONFIG_DESCR",length=MAX_LENGTH_NAME)
	private String description;
	

	

	

	

	public HibernateConfigurationProperty(){}
	public HibernateConfigurationProperty(String cd, String value, String name) {
		setCode(cd);
		setName(name);
		setValue(value);
	}

	public Integer getVersion() {
		return version;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String cd) {
		this.code = cd;
		if (isBlank(getName())) {
			setName(cd);
		}
		if (isBlank(getDescription())) {
			setDescription(cd);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = trimToNull(name);
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = trimToNull(value);
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		HibernateConfigurationProperty other = (HibernateConfigurationProperty) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	

	@Override
	public String get() {
		return this.value;
	}
	public boolean asBoolean(boolean defaultValue) {
		return support.asBoolean(defaultValue);
	}
	public Collection<String> asCollectionFromCsv(String defaultCsv) {
		return support.asCollectionFromCsv(defaultCsv);
	}
	public int asInt(int defaultValue) {
		return support.asInt(defaultValue);
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	


}
