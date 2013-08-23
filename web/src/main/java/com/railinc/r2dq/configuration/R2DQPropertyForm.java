package com.railinc.r2dq.configuration;

import static org.apache.commons.lang.StringUtils.trimToNull;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.google.common.base.Function;
import com.railinc.r2dq.domain.AuditData;
import com.railinc.r2dq.domain.ConfigurationProperty;

public class R2DQPropertyForm {

	public static final String DEFAULT_FORM_NAME = "r2dqproperty";
	
	
	Long id;
	
	@NotNull
	@Size(min=1,max=ConfigurationProperty.MAX_LENGTH_CODE)
	String code;
	
	@NotNull
	@Size(min=1,max=ConfigurationProperty.MAX_LENGTH_NAME)
	String name;

	@NotNull
	@Size(min=1,max=ConfigurationProperty.MAX_LENGTH_VALUE)
	String value;
	
	@Size(min=0,max=ConfigurationProperty.MAX_LENGTH_DESC)
	String description;
	

	@NotNull
	private Integer version;
	
	private AuditData auditData;
	
	public AuditData getAuditData() {
		return auditData;
	}

	public void setAuditData(AuditData auditData) {
		this.auditData = auditData;
	}

	public R2DQPropertyForm() {
		version = 0;
	}
	
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}


	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = trimToNull(name);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public static Function<ConfigurationProperty,R2DQPropertyForm> PROPERTY_TO_FORM = new Function<ConfigurationProperty,R2DQPropertyForm>() {
		@Override
		public R2DQPropertyForm apply(ConfigurationProperty input) {
			R2DQPropertyForm f = new R2DQPropertyForm();
			f.setCode(input.getCode());
			f.setName(input.getName());
			f.setDescription(input.getDescription());
			f.setValue(input.getValue());
			f.setAuditData((AuditData) input.getAuditData().clone());
			f.setId(input.getId());
			return f;
		}
	};
	public static final Function<R2DQPropertyForm, ConfigurationProperty> FORM_TO_NEW_PROPERTY = new Function<R2DQPropertyForm,ConfigurationProperty>() {
		@Override
		public ConfigurationProperty apply(R2DQPropertyForm i) {
			ConfigurationProperty p = new ConfigurationProperty();
			p.setCode(i.getCode());
			p.setName(i.getName());
			p.setDescription(i.getDescription());
			p.setValue(i.getValue());
			return p;
		}
		
	};

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
