package com.railinc.r2dq.identity;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import com.railinc.sso.rt.LoggedUser;
import com.railinc.sso.rt.UserService;

public class RailincSsoRuntimePreAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final UserService userService = UserService.getInstance();
	
	public RailincSsoRuntimePreAuthenticationFilter() {
		this.setCheckForPrincipalChanges(true);
		this.setInvalidateSessionOnPrincipalChange(true);
		this.setAuthenticationDetailsSource(new AuthenticationDetailsSource<HttpServletRequest, LoggedUser>() {
			@Override
			public LoggedUser buildDetails(HttpServletRequest context) {
				return UserService.getLoggedUser(context);
			}
		});
	}
	
	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		log.debug("preauthenticate");
		if (userService.isAuthenticated(request)) {
			log.debug("getting logged user from request");
			return UserService.getLoggedUser(request).getUserId();
		}
		log.debug("not authenticated, returning null");
		return null;
	}

	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
		return "n/a";
	}

}
