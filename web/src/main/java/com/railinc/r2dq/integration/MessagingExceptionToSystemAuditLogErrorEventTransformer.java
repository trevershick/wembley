package com.railinc.r2dq.integration;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;

import com.google.common.base.Function;
import com.railinc.r2dq.domain.SystemAuditLog;
import com.railinc.r2dq.log.SystemAuditLogErrorEvent;

public class MessagingExceptionToSystemAuditLogErrorEventTransformer implements Function<MessagingException, SystemAuditLogErrorEvent> {
	@Override
	public SystemAuditLogErrorEvent apply(MessagingException messagingException) {
		Message<?> failedMessage =messagingException.getFailedMessage(); 
		String entityId = null;
		
		if(failedMessage.getHeaders().get("FLOW_ENTITY_ID")!=null){
			entityId = failedMessage.getHeaders().get("FLOW_ENTITY_ID").toString();
		}
		
		SystemAuditLogErrorEvent systemAuditLog = new SystemAuditLogErrorEvent(this,(String)failedMessage.getHeaders().get("FLOW_NAME"), (String) failedMessage.getHeaders().get("FLOW_ENTITY"), entityId);
		systemAuditLog.setCause(getStackTrace(messagingException.getCause()));
		for (Method method : failedMessage.getPayload().getClass().getMethods()) {
			if (method.isAnnotationPresent(ToJson.class)) {
				try {
					systemAuditLog.setDetails( (String)method.invoke(failedMessage.getPayload(), null));
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e) {
				}
			}
		}
		
		
		return systemAuditLog;
	}
	
	private String getStackTrace(Throwable throwable){
		if(throwable ==null ){
			return null;
		}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		throwable.printStackTrace(pw);
		
		int endIndex = sw.toString().length()>SystemAuditLog.PROPERTY_ERROR_CAUSE_MAX_LENGTH? SystemAuditLog.PROPERTY_ERROR_CAUSE_MAX_LENGTH : sw.toString().length();
		return sw.toString().substring(0, endIndex);
	}
}
