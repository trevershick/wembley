package com.railinc.r2dq.log;

import java.util.HashMap;
import java.util.Map;

public class TraceContext {
	private static final ThreadLocal<Map<String, Object>> traceDetailsMap = new ThreadLocal<Map<String, Object>>();
	private static final String ENTITY_NAME = "entityName";
	private static final String ENTITY_ID = "entityId";
	

	public static void init(){
		traceDetailsMap.remove();
		traceDetailsMap.set(new HashMap<String, Object>());
	}
	
	public static void registerSource(String entityName, Long sourceId, String sourceDetails){
		init();
		traceDetailsMap.get().put(ENTITY_NAME, entityName);
		traceDetailsMap.get().put(ENTITY_ID, sourceId);
	}
	
	public static boolean hasRegisteredTrace(){
		return traceDetailsMap.get()!=null && !traceDetailsMap.get().isEmpty();
	}
	
	public static String getEnitityName(){
		return (String)traceDetailsMap.get().get(ENTITY_NAME);
	}
	
	public static Long getEnitityId(){
		return (Long)traceDetailsMap.get().get(ENTITY_ID);
	}
	
	public static void registerTo(String entityName, Long id, String details){
		traceDetailsMap.set(new HashMap<String, Object>());
	}
}
