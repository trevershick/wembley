package com.railinc.r2dq.configuration;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * puts the configuration service into the request so it can be used by JSPs
 * @author trevershick
 *
 */
public class ConfigurationServiceInterceptor implements Filter {

	private R2DQConfigurationService service;
	
	


	public R2DQConfigurationService getService() {
		return service;
	}

	@Required
	public void setService(R2DQConfigurationService service) {
		this.service = service;
	}



	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		service = WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext()).getBean(R2DQConfigurationService.class);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		request.setAttribute("configurationService", service);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		
		
	}

}
