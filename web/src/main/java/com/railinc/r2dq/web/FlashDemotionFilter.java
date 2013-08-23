package com.railinc.r2dq.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.validation.BindingResult;

/**
 * Servlet Filter implementation class FlashDemotionFilter
 */
public class FlashDemotionFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest h = (HttpServletRequest) request;
		HttpSession session = h.getSession(false);
		// no session, short circuit
 		if (session == null) {
			chain.doFilter(request, response);
			return;
		}
		
		
 		BindingResult flashMessages = (BindingResult) session.getAttribute(FlashMessages.FLASH_MESSAGE_SESSION_ATTR);
		BindingResult flashErrors = (BindingResult) session.getAttribute(FlashMessages.FLASH_ERROR_SESSION_ATTR);
		
		if (flashMessages != null) {
			session.removeAttribute(FlashMessages.FLASH_MESSAGE_SESSION_ATTR);
			request.setAttribute(FlashMessages.FLASH_MESSAGE_SESSION_ATTR, flashMessages); // simple promotion
		}
		if (flashErrors != null) {
			session.removeAttribute(FlashMessages.FLASH_ERROR_SESSION_ATTR);
			request.setAttribute(FlashMessages.FLASH_ERROR_SESSION_ATTR, flashErrors); // simple promotion
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		

	}

}
