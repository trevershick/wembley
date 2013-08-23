package com.railinc.notifserv.rest.r2009v1.services;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;

public abstract class BaseServiceImpl {
	protected static final String CONTENTYPE_TEXT_XML = "text/xml";

	protected static final String HTTP_500_ERROR_XML = "<error>%s</error>";
	protected static final String HTTP_404_ERROR_XML = "<error>404 - Resource Not Found</error>";
	protected static final String HTTP_400_ERROR_XML = "<error>400 - Bad Request (%s)</error>";
	protected static final String HTTP_409_ERROR_XML = "<error>409 - Conflict (%s)</error>";
	
	protected abstract Logger getLog();
	
	
	
	public void throw500(Exception e) {
		e.printStackTrace();
		logException(e);
		throw new WebApplicationException(Response.status( Status.INTERNAL_SERVER_ERROR)
			.type(MediaType.TEXT_XML)
			.entity( String.format(HTTP_500_ERROR_XML,e.toString()) ).build());
	}
	
	public void throw500(String name) {
		throw new WebApplicationException( Response.status( HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
				.type(MediaType.TEXT_XML)
				.entity( String.format(HTTP_500_ERROR_XML,name) ).build() );
	}

	public void throw400(String message) {
		throw new WebApplicationException( 
				Response.status(HttpServletResponse.SC_BAD_REQUEST)
				.type(MediaType.TEXT_XML)
				.entity( String.format(HTTP_400_ERROR_XML,message) ).build() );
	}
	public void throw409Conflict(String msg) {
		throw new WebApplicationException( 
				Response.status(HttpServletResponse.SC_CONFLICT)
					.type(MediaType.TEXT_XML)
					.entity( String.format(HTTP_409_ERROR_XML,msg) ).build() );
	}
	
	
	public void throw404IfNull(Object obj) {
		if (null == obj) {
			throw new WebApplicationException( 
					Response.status( HttpServletResponse.SC_NOT_FOUND )
						.type(MediaType.TEXT_XML)
						.entity( HTTP_404_ERROR_XML ).build() );
		}
	}
	public void throwBadRequestIfNull(Object obj, String arg) {
		String tmp = arg + " is required";
		if (null == obj) {
			throw new WebApplicationException( 
					Response.status( HttpServletResponse.SC_BAD_REQUEST )
						.type(MediaType.TEXT_XML)
						.entity( String.format(HTTP_400_ERROR_XML, tmp) ).build() );
		}
	}
	

	protected void require(String name, Object object) {
		if (null == object) {
			throw500(name + " is required for " + getClass().getSimpleName());
		}
	}


	protected void logException( Throwable exception ) {
		
		if ( exception != null )
		{
			getLog().error( exception.getMessage() );
		
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter( sw );
	
			exception.printStackTrace( pw );
			getLog().error( sw.toString() );
		}
	}
}
