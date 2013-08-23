package com.railinc.r2dq.integration;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.channel.interceptor.ChannelInterceptorAdapter;
import org.springframework.integration.support.MessageBuilder;

public class FlowInterceptor extends ChannelInterceptorAdapter {
	
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		Object payload = message.getPayload();
		
		if (!payload.getClass().isAnnotationPresent(FlowEntity.class)) {
			return message;
		}
		
		String FLOW_ENTITY = "";
		Object FLOW_ENTITY_ID = "";
		String FLOW_ENTITY_DATA = "";
		FLOW_ENTITY = payload.getClass().getSimpleName();
		
		FLOW_ENTITY_ID = getFlowEntityId(payload.getClass(), payload);
		
		if(FLOW_ENTITY_ID == null){
			FLOW_ENTITY_ID = getFlowEntityId(payload.getClass().getSuperclass(), payload);
		}
		
		for (Method method : payload.getClass().getMethods()) {
			if (method.isAnnotationPresent(ToJson.class)) {
				try {
					FLOW_ENTITY_DATA = (String)method.invoke(payload, new Object[0]);
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e) {
				}
			}
		}
		
		return MessageBuilder.fromMessage(message).
				setHeader("FLOW_ENTITY", FLOW_ENTITY).
				setHeader("FLOW_ENTITY_ID", FLOW_ENTITY_ID).
				setHeader("FLOW_ENTITY_DATA", FLOW_ENTITY_DATA).
				build();
	}
	
	private Object getFlowEntityId(Class<?> fieldClass, Object payload){
		for(Field field:fieldClass.getDeclaredFields()){
			if (field.isAnnotationPresent(FlowEntityId.class)) {
				try {
					field.setAccessible(true);
					return field.get(payload);
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				}
			}
		}
		return null;
	}
	
	@Override
	public void postSend(Message<?> message, MessageChannel channel,boolean sent) {
		super.postSend(message, channel, sent);
	}
	
}
