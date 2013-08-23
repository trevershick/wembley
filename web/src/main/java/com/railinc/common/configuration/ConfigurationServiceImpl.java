package com.railinc.common.configuration;

import static org.apache.commons.lang.StringUtils.defaultIfBlank;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=true)
public class ConfigurationServiceImpl implements ConfigurationService {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private PropertyService<? extends ConfigurationProperty> propertyService;
	
	private static final String ENVIRONMENT = "environment";
	
	private static final String ENVIRONMENT_DEV = "dev";
//	private static final String ENVIRONMENT_TST = "test";
	private static final String ENVIRONMENT_PROD = "prod";
//	private static final String ENVIRONMENT_QA = "qa";
	private static final String DEFAULT_ENVIRONMENT = ENVIRONMENT_PROD;

	
	
	
	
	public PropertyService<? extends ConfigurationProperty> getPropertyService() {
		return propertyService;
	}

	@Required
	public void setPropertyService(PropertyService<? extends ConfigurationProperty> propertyService) {
		this.propertyService = propertyService;
	}
	@Override
	public boolean isDevelopment() {
		String environment = defaultIfBlank(getStringValue(ENVIRONMENT), DEFAULT_ENVIRONMENT);
		return StringUtils.equals(ENVIRONMENT_DEV, environment);
	}

	

	/**
	 * Looks first for System.getProperty( "railinc.global." + propertyname)
	 * then for System.getProperty(propertyName)
	 * then in the database, then JNDI as a last resort
	 * 
	 * @param parameterName
	 * @param defaultIfBlank
	 * @return
	 */
	protected String overridable(String parameterName, String defaultIfBlank) {
		String vmEnvironmentVariableName = String.format("railinc.global.%s", parameterName);
		String vmEnvironmentVariable = System.getProperty(vmEnvironmentVariableName);
		if (StringUtils.isNotBlank(vmEnvironmentVariable)) {
			return vmEnvironmentVariable;
		}
		ConfigurationProperty configurationValue = getConfigurationValue(parameterName);

		if (configurationValue != null) {
			return configurationValue.getValue();
		}
		
		return StringUtils.defaultIfBlank(lookupInJndi(parameterName), defaultIfBlank);
	}
	/**
	 * @param parameterName
	 * @return null if not found or exception occurs
	 */
	protected String lookupInJndi(String parameterName) {
		String name = "java:comp/env/env/".concat(parameterName);
		try {
			InitialContext ctx = new InitialContext();
			Object lookup = ctx.lookup(name);
			return String.valueOf(lookup);
		} catch (NameNotFoundException nnfe) {
			log.debug("Name not found in JNDI (maybe ok) : " + name);
			return null;
		} catch (Exception e) {
			log.debug("Looking up java:comp/env/env/" + parameterName, e);
			return null;
		}
	}
	
	protected Collection<String> overridableCollection(String parameterName, String defaultCsv) {
		String vmEnvironmentVariableName = String.format("railinc.global.%s", parameterName);
		String vmEnvironmentVariable = System.getProperty(vmEnvironmentVariableName);
		if (StringUtils.isNotBlank(vmEnvironmentVariable)) {
			return ConfigurationPropertySupport.split(vmEnvironmentVariable);
		}
		ConfigurationProperty configurationValue = getConfigurationValue(parameterName);
		if (configurationValue == null) {
			return ConfigurationPropertySupport.split(defaultCsv);
		}

		return configurationValue.asCollectionFromCsv(defaultCsv);
	}
	
	
	/**
	 * @see #overridable(String, String)
	 */
	protected int overridable(String parameterName, int defaultIfMissing) {
		String vmEnvironmentVariableName = String.format("railinc.global.%s", parameterName);
		String vmEnvironmentVariable = System.getProperty(vmEnvironmentVariableName);
		if (StringUtils.isNotBlank(vmEnvironmentVariable)) {
			try {
				int value = Integer.parseInt(vmEnvironmentVariable);
				return value;
			} catch (NumberFormatException nfe) {
				log.error("VM Parameter " + vmEnvironmentVariableName + " is set to " + vmEnvironmentVariable + " but an integer value is expected");
			}
		}
		ConfigurationProperty configurationValue = getConfigurationValue(parameterName);
		if (configurationValue == null) {
			return defaultIfMissing;
		}

		return configurationValue.asInt(defaultIfMissing);
	}
	
	/**
	 * @see #overridable(String, String)
	 */
	protected boolean overridable(String parameterName, boolean defaultIfMissing) {
		String vmEnvironmentVariableName = String.format("railinc.global.%s", parameterName);
		String vmEnvironmentVariable = System.getProperty(vmEnvironmentVariableName);
		if (StringUtils.isNotBlank(vmEnvironmentVariable)) {
			return Boolean.valueOf(vmEnvironmentVariable);
		}
		ConfigurationProperty configurationValue = getConfigurationValue(parameterName);
		if (configurationValue == null) {
			return defaultIfMissing;
		}

		return configurationValue.asBoolean(defaultIfMissing);
	}
	
	Map<String, ConfigurationProperty> values() {
		HashMap<String, ConfigurationProperty> tmp = new HashMap<String, ConfigurationProperty>();
		Collection<? extends ConfigurationProperty> l = getConfigurationValues();
		for (ConfigurationProperty cv : l) {
			tmp.put(cv.getCode(), cv);
		}
		return tmp;
	}

	private ConfigurationProperty getConfigurationValue(String key) {
		return values().get(key);
	}

	private Collection<? extends ConfigurationProperty> getConfigurationValues() {
		return propertyService.all(null);
	}



	/**
	 * 
	 * @param key
	 * @return null if not found
	 */
	public String getStringValue(String key) {
		ConfigurationProperty configurationValue = getConfigurationValue(key);
		String tmp = null;
		if (configurationValue != null) {
			tmp = configurationValue.getValue();
		}
		return tmp;
	}



}
