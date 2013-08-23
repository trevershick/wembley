package com.railinc.r2dq.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;

public class GsonUtil {
	
	
	public static <T> T fromJson(String jsonString, Class<T> classOfT) {
		return new GsonBuilder().setDateFormat("yyyy-mm-dd HH:MM").create().fromJson(jsonString, classOfT);
	  }
	
	public static String toJson(Object obj){
		return toJson(obj, null);
	}
	public static String toJson(Object obj, String fieldsToIgnore){
		Validate.notNull(obj);
		 GsonBuilder builder = new GsonBuilder().setDateFormat("yyyy-mm-dd HH:MM");
		if(StringUtils.isNotBlank(fieldsToIgnore)){
			builder.setExclusionStrategies(new GsonFiledExclusionStrategy(obj.getClass(), fieldsToIgnore));
		}
		return builder.create().toJson(obj);
	}
	
	private static class GsonFiledExclusionStrategy implements ExclusionStrategy {

        private Map<Class<?>, List<String>> excludedFields = new HashMap<Class<?>, List<String>>(); 
        
		public GsonFiledExclusionStrategy(Class<?> className, String fieldsToIgnore) 
        {
            setExcludeFields(className, fieldsToIgnore);
        }
        
        public boolean shouldSkipClass(Class<?> arg0) {
            return false;
        }

        public boolean shouldSkipField(FieldAttributes f) {
        	return excludedFields.containsKey(f.getDeclaringClass()) &&	excludedFields.get(f.getDeclaringClass()).contains(f.getName());
        }
        
        private void setExcludeFields(Class<?> className, String fieldsToIgnore){
        	for(String fieldToIgnore: fieldsToIgnore.split(",")){
        		 if(StringUtils.isNotBlank(fieldToIgnore)){
        			 setClassName(className, fieldToIgnore.trim());
        		 }
        	}
        }
        
        @SuppressWarnings("rawtypes")
		private void setClassName(Class<?> className, String fieldToIgnore){
        	List<Field> fields = new ArrayList<Field>();
        	
        	fields.addAll(Arrays.asList(className.getDeclaredFields()));
        	fields.addAll(Arrays.asList(className.getSuperclass().getDeclaredFields()));
        	
        	String[] fieldName = fieldToIgnore.split("\\.");
        	if(fieldName.length<=1){
        		List<String> fieldNames = excludedFields.get(className);
        		if(fieldNames == null){
        			fieldNames = new ArrayList<String>();
        		}
        		fieldNames.add(fieldToIgnore);
        		excludedFields.put(className, fieldNames);
        		excludedFields.put(className.getSuperclass(), fieldNames);
        		return;
        	}
        	for (Field field : fields) {        		
        		if(!field.getName().equals(fieldName[0])){
        			continue;
        		}
        	  Type type = field.getGenericType();
        	  if (type instanceof ParameterizedType) {
        	    ParameterizedType ptype = (ParameterizedType) type;
        	    setClassName((Class)ptype.getActualTypeArguments()[0], fieldName[1]);
        	  } else {
        	    setClassName((Class)type, fieldName[1]);
        	  }
        	}
        }
    }

}
