package com.railinc.r2dq.support;

import java.io.InputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.ModelAttribute;

public class SupportControllerBaseImpl  implements ApplicationContextAware {
	
	String address;
	String hostname;
	String versionTxt;
	List<String> revisionText;
	Random random = null;
//	ConfigurationService configurationService;
	private ApplicationContext applicationContext;
	
	public SupportControllerBaseImpl() {
		random = new Random(System.currentTimeMillis());
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			hostname = localHost.getHostName();
			address = localHost.getHostAddress();
		} catch (Exception e) {
			address = hostname = e.getMessage();
		}		
	}
	
//	/**
//	 * @return the configurationService
//	 */
//	public ConfigurationService getConfigurationService() {
//		if (configurationService == null) {
//			throw new IllegalStateException("configurationService is not set");
//		}
//		return configurationService;
//	}

//	/**
//	 * @param configurationService the configurationService to set
//	 */
//	public void setConfigurationService(ConfigurationService configurationService) {
//		this.configurationService = configurationService;
//	}

	@ModelAttribute("svnRevisionText")
	public List<String> getSvnRevisionText() {
		if (revisionText == null) {
		List<String> txt = new ArrayList<String>();
		try {
			InputStream resourceAsStream = getClass().getResourceAsStream("/svn-revision.txt");
			txt = org.apache.commons.io.IOUtils.readLines(resourceAsStream);
		} catch (Exception e) {
			txt.add("Not Available : " + e.getMessage());
		}
		revisionText = txt;
		}
		return revisionText;
	}
	
	@ModelAttribute("versionText")
	public String getVersionText() {
		if (versionTxt == null) {
			versionTxt = Application.getVersion();
		}
		return this.versionTxt;
	}
	

	@ModelAttribute("hostname")
	public String hostname() {
		return hostname;
	}
	
	@ModelAttribute("address")
	public String address() {
		return this.address;
	}
	

	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		
	}
	
	public ApplicationContext applicationContext() {
		return this.applicationContext;
	}
}
