package com.railinc.wembley.api.util;

public class SchemaNameLocator {

	private static final String SCHEMA_NAME_PROP_KEY = "not.serv.schema.name";

	public static String getNotificationServiceSchemaName() {
		String value = System.getProperty(SCHEMA_NAME_PROP_KEY);
		if(value == null) {
			value = "";
		} else {
			if(!value.endsWith(".")) {
				value = value.concat(".");
			}
		}

		return value;
	}
}
