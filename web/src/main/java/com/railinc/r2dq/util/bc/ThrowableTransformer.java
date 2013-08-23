package com.railinc.r2dq.util.bc;

import java.util.Collection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ThrowableTransformer {

	@SuppressWarnings("unchecked")
	public JSONObject apply(Throwable t) {
		JSONObject o = new JSONObject();
		
		o.put("message", t.getMessage());
		
		JSONArray a = new JSONArray();
		StackTraceElement[] stackTrace = t.getStackTrace();
		for (StackTraceElement ste : stackTrace) {
			JSONObject so = new JSONObject();
			so.put("className", ste.getClassName());
			so.put("fileName", ste.getFileName());
			so.put("lineNumber", ste.getLineNumber());
			so.put("methodName", ste.getMethodName());
			a.add(so);
		}
		o.put("stackTrace", a);
		
		return o;
	}

	@SuppressWarnings("unchecked")
	public JSONArray apply(Collection<Throwable> failureExceptions) {
		JSONArray a = new JSONArray();
		for (Throwable t : failureExceptions) {
			a.add(apply(t));
		}
		return a;
	}
}
