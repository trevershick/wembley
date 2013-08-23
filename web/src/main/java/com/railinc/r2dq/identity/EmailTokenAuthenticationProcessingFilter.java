package com.railinc.r2dq.identity;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import com.railinc.r2dq.identity.R2DQPrincipal.EmailTokenPrincipal;

public class EmailTokenAuthenticationProcessingFilter extends GenericFilterBean {
	private IdentityService identityService;
	
    private AuthenticationEntryPoint authenticationEntryPoint;

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        final boolean debug = logger.isDebugEnabled();
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        if (!hasEmailToken(request)) {
            chain.doFilter(request, response);
            return;
        }


        try {
            Authentication authResult = attemptAuthentication(request, response);
            SecurityContextHolder.getContext().setAuthentication(authResult);

            if (debug) {
                logger.debug("Authentication success: " + authResult);
            }

            onSuccessfulAuthentication(request, response, authResult);
        } catch (AuthenticationException failed) {
            SecurityContextHolder.clearContext();

            if (debug) {
                logger.debug("Authentication request for failed: " + failed);
            }

            onUnsuccessfulAuthentication(request, response, failed);

            authenticationEntryPoint.commence(request, response, failed);

            return;
        }

    }

   


    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            Authentication authResult) throws IOException {
		String emailToken = (String) request.getAttribute("__R2DQ_SUPPLIED_EMAIL_TOKEN__");
		request.removeAttribute("__R2DQ_SUPPLIED_EMAIL_TOKEN__");
		
		String requestURI = request.getRequestURI();
		requestURI = requestURI.replaceAll("-"+ emailToken +"-/", "");
		response.sendRedirect(requestURI);
    	
    }

    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException {
    }

    protected AuthenticationEntryPoint getAuthenticationEntryPoint() {
        return authenticationEntryPoint;
    }

    public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

	protected Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		String emailToken = (String) request.getAttribute("__R2DQ_SUPPLIED_EMAIL_TOKEN__");
		EmailTokenPrincipal r2dqPrincipal = new R2DQPrincipal.EmailTokenPrincipal(emailToken, identityService);
		if (r2dqPrincipal.hasNoIdentity()) {
			throw new AuthenticationCredentialsNotFoundException("invalid token");
		}
		return new PreAuthenticatedAuthenticationToken(r2dqPrincipal, 
				emailToken,
				r2dqPrincipal.authorities());
	}


	protected boolean hasEmailToken(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		Pattern p = Pattern.compile("-ea-([^-]+)-");
		Matcher matcher = p.matcher(requestURI);
		if (matcher.find()) {
			request.setAttribute("__R2DQ_SUPPLIED_EMAIL_TOKEN__", matcher.group(1));
			return true;
		}
		return false;

	}


	public IdentityService getIdentityService() {
		return identityService;
	}

	@Required
	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}
}
